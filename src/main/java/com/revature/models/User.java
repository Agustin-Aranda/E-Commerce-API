package com.revature.models;

public class User {
    private int UserId;

    private String FirstName;

    private String LastName;

    private String Email;

    private String Password;

    private Role role;

    public User(String firstName, String lastName, String email, String password){
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.Password = password;

        // We don't want to take in userId or role because we want these to be set to a default value
        this.role = Role.USER;
    }

    public User() {

    }

    // Getters and Setters
    public int getUserId() { return UserId;}

    public void setUserId(int userId) { UserId = userId;}

    public String getLastName() { return LastName; }

    public void setLastName(String lastName) { LastName = lastName;}

    public String getFirstName() { return FirstName;}

    public void setFirstName(String firstName) { FirstName = firstName;}

    public String getEmail() { return Email;}

    public void setEmail(String email) { Email = email;}

    public String getPassword() { return Password;}

    public void setPassword(String password) { Password = password;}

    public Role getRole() { return role;}

    public void setRole(Role role) { this.role = role; }
}
