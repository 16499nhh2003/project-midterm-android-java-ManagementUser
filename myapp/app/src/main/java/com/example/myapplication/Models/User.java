package com.example.myapplication.Models;

public class User {
    private String name;
    private int Age;
    private String phoneNumber;
    private boolean status;

    public User(String name, int age, String phoneNumber, boolean status) {
        this.name = name;
        Age = age;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public User() {
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
}
