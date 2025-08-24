package com.example.easyevent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.logicsystemandsolutions.EasyEvent.HomeActivity;
import com.logicsystemandsolutions.EasyEvent.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyBookingsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private TextView userName;
    private ImageView userImage;
    private LinearLayout bookingsContainer;
    private LinearLayout emptyState;

    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;
    private ArrayList<HashMap<String, String>> bookingData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        initViews();
        setupFirebase();
        setupToolbar();
        setupNavigationDrawer();
        loadBookings();
        updateUserInfo();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        bookingsContainer = findViewById(R.id.bookingsContainer);
        emptyState = findViewById(R.id.emptyState);
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("coding_competition");
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Bookings");
        }
    }

    private void setupNavigationDrawer() {
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Setup navigation header
        View headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.userName);
        userImage = headerView.findViewById(R.id.userImage);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Intent intent = new Intent(MyBookingsActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_profile) {
                    Toast.makeText(getApplicationContext(), "Profile - Coming Soon", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_events) {
                    Toast.makeText(getApplicationContext(), "Already viewing bookings", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    logoutUser();
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void loadBookings() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            showEmptyState();
            return;
        }

        String userId = currentUser.getUid();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingData.clear();
                bookingsContainer.removeAllViews();

                boolean hasBookings = false;

                for (DataSnapshot participantSnapshot : snapshot.getChildren()) {
                    String uid = participantSnapshot.child("uid").getValue(String.class);

                    if (uid != null && uid.equals(userId)) {
                        hasBookings = true;

                        String name = participantSnapshot.child("name").getValue(String.class);
                        String mobile = participantSnapshot.child("mobile").getValue(String.class);
                        String college = participantSnapshot.child("college").getValue(String.class);
                        String participantId = participantSnapshot.getKey();

                        HashMap<String, String> booking = new HashMap<>();
                        booking.put("event", "Coding Competition");
                        booking.put("id", participantId);
                        booking.put("name", name != null ? name : "Unknown");
                        booking.put("mobile", mobile != null ? mobile : "");
                        booking.put("college", college != null ? college : "");

                        bookingData.add(booking);
                        addBookingView(booking);
                    }
                }

                if (!hasBookings) {
                    showEmptyState();
                } else {
                    hideEmptyState();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error loading bookings", Toast.LENGTH_LONG).show();
                showEmptyState();
            }
        });
    }

    private void addBookingView(HashMap<String, String> booking) {
        View bookingView = LayoutInflater.from(this).inflate(R.layout.booking_item, bookingsContainer, false);

        TextView eventName = bookingView.findViewById(R.id.eventName);
        TextView participantName = bookingView.findViewById(R.id.participantName);
        TextView bookingStatus = bookingView.findViewById(R.id.bookingStatus);

        eventName.setText(booking.get("event"));
        participantName.setText(booking.get("name"));
        bookingStatus.setText("Confirmed");

        bookingView.setOnClickListener(v -> {
            Intent intent = new Intent(MyBookingsActivity.this, QRCodeActivity.class);
            intent.putExtra("event", booking.get("event"));
            intent.putExtra("id", booking.get("id"));
            intent.putExtra("name", booking.get("name"));
            intent.putExtra("mobile", booking.get("mobile"));
            intent.putExtra("college", booking.get("college"));
            startActivity(intent);
        });

        bookingsContainer.addView(bookingView);
    }

    private void showEmptyState() {
        emptyState.setVisibility(View.VISIBLE);
        bookingsContainer.setVisibility(View.GONE);
    }

    private void hideEmptyState() {
        emptyState.setVisibility(View.GONE);
        bookingsContainer.setVisibility(View.VISIBLE);
    }

    private void updateUserInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            String email = currentUser.getEmail();

            if (displayName != null && !displayName.isEmpty()) {
                userName.setText("Welcome, " + displayName);
            } else if (email != null) {
                userName.setText("Welcome, " + email.split("@")[0]);
            } else {
                userName.setText("Welcome, Student");
            }
        } else {
            userName.setText("Welcome, Guest");
        }
    }

    private void logoutUser() {
        mAuth.signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MyBookingsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }
}