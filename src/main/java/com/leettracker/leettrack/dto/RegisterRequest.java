package com.leettracker.leettrack.dto;

public class RegisterRequest {

    String name;
    String email;
    String password;

    public String getEmail(){
        return email;
    }

    public String getName(){
        return name;
    }

    public String getPassword(){
        return password;
    }
}
