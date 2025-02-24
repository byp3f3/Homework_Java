package com.example.authp1;

public class AdminLog {
    private String action;
    private String email;
    private String role;
    private long timestamp;

    public AdminLog() {
        // Пустой конструктор необходим для Firestore
    }

    public AdminLog(String action, String email, String role, long timestamp) {
        this.action = action;
        this.email = email;
        this.role = role;
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public long getTimestamp() {
        return timestamp;
    }
}