package com.example.easyevent ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.logicsystemandsolutions.EasyEvent.R;

public class SignupActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhone, etPassword;
    Button btnSignup;

    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.logicsystemandsolutions.EasyEvent.R.layout.activity_signup);

        // Initialize Firebase reference with SignupCode
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("SignupCode");

        etName = findViewById(R.id.fullNameEditText);
        etEmail = findViewById(R.id.emailEditText);
        etPhone = findViewById(R.id.phoneEditText);
        etPassword = findViewById(R.id.passwordEditText);
        btnSignup = findViewById(R.id.sendOtpButton);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a user object
                    com.example.logicandsolutions.User user = new com.example.logicandsolutions.User(name, email, phone, password);

                    // Generate unique ID
                    String userId = ref.push().getKey();

                    if (userId != null) {
                        ref.child(userId).setValue(user)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                                        etName.setText("");
                                        etEmail.setText("");
                                        etPhone.setText("");
                                        etPassword.setText("");
                                    } else {
                                        Toast.makeText(SignupActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }
            }
        });
    }
}
