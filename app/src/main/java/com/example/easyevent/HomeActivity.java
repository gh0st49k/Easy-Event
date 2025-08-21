package com.logicsystemandsolutions.EasyEvent;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.easyevent.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private TextView userName;
    private ImageView userImage;
    private Button logoutButton;
    private ViewFlipper viewFlipper;

    // Category layouts
    private LinearLayout computerCategory, mechanicalCategory, civilCategory, entcCategory, aimlCategory;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupFirebase();
        setupToolbar();
        setupNavigationDrawer();
        setupClickListeners();
        setupViewFlipper();
        updateUserInfo();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        logoutButton = findViewById(R.id.logoutBtn);
        viewFlipper = findViewById(R.id.homeSlider);

        // Category layouts
        computerCategory = findViewById(R.id.computerCategory);
        mechanicalCategory = findViewById(R.id.mechanicalCategory);
        civilCategory = findViewById(R.id.civilCategory);
        entcCategory = findViewById(R.id.entcCategory);
        aimlCategory = findViewById(R.id.aimlCategory);
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Easy Event");
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
                    Toast.makeText(getApplicationContext(), "Already on Home", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_profile) {
                    // TODO: Navigate to Profile Activity
                    Toast.makeText(getApplicationContext(), "Profile - Coming Soon", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_events) {
                    // TODO: Navigate to Events Activity
                    Toast.makeText(getApplicationContext(), "Events - Coming Soon", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    logoutUser();
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void setupClickListeners() {
        logoutButton.setOnClickListener(v -> logoutUser());

        // Category click listeners
        computerCategory.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, com.logicsystemandsolutions.EasyEvent.ComputerBranchActivity.class);
            startActivity(intent);
        });

        mechanicalCategory.setOnClickListener(v -> {
            Toast.makeText(this, "Mechanical Events - Coming Soon", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Mechanical Events
        });

        civilCategory.setOnClickListener(v -> {
            Toast.makeText(this, "Civil Events - Coming Soon", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Civil Events
        });

        entcCategory.setOnClickListener(v -> {
            Toast.makeText(this, "ENTC Events - Coming Soon", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to ENTC Events
        });

        aimlCategory.setOnClickListener(v -> {
            Toast.makeText(this, "AIML Events - Coming Soon", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to AIML Events
        });
    }

    private void setupViewFlipper() {
        if (viewFlipper != null) {
            viewFlipper.startFlipping();
        }
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
                userName.setText("Welcome, User");
            }
        } else {
            userName.setText("Welcome, Guest");
        }
    }

    private void logoutUser() {
        mAuth.signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not signed in, redirect to login
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