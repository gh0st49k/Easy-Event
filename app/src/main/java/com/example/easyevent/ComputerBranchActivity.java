package com.logicsystemandsolutions.EasyEvent;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.easyevent.CodingCompetitionActivity;
import com.example.easyevent.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ComputerBranchActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private TextView userName;
    private ImageView userImage;
    private Button logoutButton;

    // Event buttons
    private Button codingCompButton, hackathonButton, webDevButton,
            aiMlButton, dataStructureButton, algorithmButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_branch);

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
        logoutButton = findViewById(R.id.logoutBtn);

        // Event buttons
        codingCompButton = findViewById(R.id.codingCompButton);
        hackathonButton = findViewById(R.id.hackathonButton);
        webDevButton = findViewById(R.id.webDevButton);
        aiMlButton = findViewById(R.id.aiMlButton);
        dataStructureButton = findViewById(R.id.dataStructureButton);
        algorithmButton = findViewById(R.id.algorithmButton);
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Computer Branch Events");
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
                    Intent intent = new Intent(ComputerBranchActivity.this, com.logicsystemandsolutions.EasyEvent.HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_profile) {
                    Toast.makeText(getApplicationContext(), "Profile - Coming Soon", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_events) {
                    Toast.makeText(getApplicationContext(), "Already viewing events", Toast.LENGTH_SHORT).show();
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

        codingCompButton.setOnClickListener(v -> {
            Intent intent = new Intent(ComputerBranchActivity.this, CodingCompetitionActivity.class);
            startActivity(intent);
        });

        hackathonButton.setOnClickListener(v -> {
            Toast.makeText(this, "Hackathon - Coming Soon", Toast.LENGTH_SHORT).show();
        });

        webDevButton.setOnClickListener(v -> {
            Toast.makeText(this, "Web Development - Coming Soon", Toast.LENGTH_SHORT).show();
        });

        aiMlButton.setOnClickListener(v -> {
            Toast.makeText(this, "AI/ML Workshop - Coming Soon", Toast.LENGTH_SHORT).show();
        });

        dataStructureButton.setOnClickListener(v -> {
            Toast.makeText(this, "Data Structures - Coming Soon", Toast.LENGTH_SHORT).show();
        });

        algorithmButton.setOnClickListener(v -> {
            Toast.makeText(this, "Algorithm Contest - Coming Soon", Toast.LENGTH_SHORT).show();
        });
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

        Intent intent = new Intent(ComputerBranchActivity.this, LoginActivity.class);
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