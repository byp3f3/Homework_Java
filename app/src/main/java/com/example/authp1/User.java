package com.example.authp1;

public class User {
    private String id;
    private String email;
    private String role;

    public User(){}

    public User(String id, String email, String role){
        this.role = role;
        this.id = id;
        this.email = email;

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}
