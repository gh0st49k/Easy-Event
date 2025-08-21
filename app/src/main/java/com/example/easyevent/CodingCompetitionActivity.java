package com.example.easyevent;
import com.example.easyevent.models.EventParticipant;
import com.example.easyevent.utils.DatabaseHelper;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.logicsystemandsolutions.EasyEvent.ComputerBranchActivity;
import com.logicsystemandsolutions.EasyEvent.HomeActivity;
import com.logicsystemandsolutions.EasyEvent.R;

import java.util.HashMap;
import java.util.Map;

public class CodingCompetitionActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private TextView userName;
    private ImageView userImage;
    private Button bookTicketButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coding_competion);

        initViews();
        setupFirebase();
        setupToolbar();
        setupNavigationDrawer();
        setupClickListeners();
        updateUserInfo();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        bookTicketButton = findViewById(R.id.bookTicketButton);
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Coding Competition");
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
                    Intent intent = new Intent(CodingCompetitionActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == com.logicsystemandsolutions.EasyEvent.R.id.nav_profile) {
                    Toast.makeText(getApplicationContext(), "Profile - Coming Soon", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_events) {
                    Intent intent = new Intent(CodingCompetitionActivity.this, ComputerBranchActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_logout) {
                    logoutUser();
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void setupClickListeners() {
        bookTicketButton.setOnClickListener(v -> showEntryDialog());
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

    private void showEntryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.entry_dialog, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        EditText dialogName = dialogView.findViewById(R.id.dialogName);
        EditText dialogMobile = dialogView.findViewById(R.id.dialogMobile);
        EditText dialogCollege = dialogView.findViewById(R.id.dialogCollege);
        EditText dialogSem = dialogView.findViewById(R.id.dialogSem);
        Button dialogSubmit = dialogView.findViewById(R.id.dialogSubmit);

        dialogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = dialogName.getText().toString().trim();
                String mobile = dialogMobile.getText().toString().trim();
                String college = dialogCollege.getText().toString().trim();
                String sem = dialogSem.getText().toString().trim();

                if (name.isEmpty() || mobile.isEmpty() || college.isEmpty() || sem.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                    return;
                }

                if (mobile.length() < 10) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.dismiss();
                processRegistration(name, mobile, college, sem);
            }
        });
    }

    private void processRegistration(String name, String mobile, String college, String semester) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        bookTicketButton.setEnabled(false);
        bookTicketButton.setText("Processing...");

        // Create participant object
        EventParticipant participant = new EventParticipant(
                null, // participantId will be generated by push()
                name,
                mobile,
                college,
                semester
        );

        // Save participant to Firebase
        DatabaseHelper.getInstance().saveEventParticipant(
                DatabaseHelper.CODING_COMPETITION_PATH,
                participant,
                new DatabaseHelper.DatabaseCallback() {
                    @Override
                    public void onSuccess(String message) {
                        // Reset button state
                        bookTicketButton.setEnabled(true);
                        bookTicketButton.setText("Book Ticket");

                        // Show success dialog
                        new AlertDialog.Builder(CodingCompetitionActivity.this)
                                .setTitle("Registration Successful 🎉")
                                .setMessage("Welcome " + name + "!\n\n" +
                                        "Event: Coding Competition\n" +
                                        "Fee: ₹20\n\n" +
                                        "Please bring your student ID and laptop on the event day.")
                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                .setCancelable(false)
                                .show();
                    }

                    @Override
                    public void onFailure(String error) {  // ✅ Correct method name
                        // Reset button state
                        bookTicketButton.setEnabled(true);
                        bookTicketButton.setText("Book Ticket");

                        // Show error
                        Toast.makeText(CodingCompetitionActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


    private void logoutUser() {
        mAuth.signOut();
// Inside logoutUser() method
        Intent intent = new Intent(this, com.example.easyevent.SignupActivity.class);        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
