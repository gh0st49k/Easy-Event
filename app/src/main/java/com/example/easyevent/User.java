package com.example.logicandsolutions;

public class User {
    public String name, email, phone, password;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }
}
