package com.logicsystemandsolutions.EasyEvent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easyevent.LoginActivity;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView logoImage;
    private TextView appTitle, appSubtitle, appDescription;
    private Button getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        logoImage = findViewById(R.id.logoImage);

        appSubtitle = findViewById(R.id.appSubtitle);
        appDescription = findViewById(R.id.appDescription);
        getStartedButton = findViewById(R.id.getStartedButton);
    }

    private void setupClickListeners() {
        getStartedButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}