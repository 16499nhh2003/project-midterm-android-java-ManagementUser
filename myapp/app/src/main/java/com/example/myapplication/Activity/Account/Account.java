package com.example.myapplication.Activity.Account;

public class Account {
    private String id;

    private String name;
    private int Age;
    private String phoneNumber;
    private boolean status; //  true : normal   , false : locked
    private String url;


    private String email;
    private String role; // 1 : manager  :2 employee

    public Account(String name, int age, String phoneNumber, String email) {
        this.name = name;
        this.Age = age;
        this.phoneNumber = phoneNumber;
        this.status = true;
        this.email = email;
    }

    public Account() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", Age=" + Age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status=" + status +
                ", url='" + url + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
