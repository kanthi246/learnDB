package com.example.lenovo.learndb;

import com.j256.ormlite.field.DatabaseField;

import java.sql.Blob;

public class UserDetails {
    //(columnName = "name")
    @DatabaseField
    private String name;

    @DatabaseField
    private String email;

    @DatabaseField
    private String password;

    @DatabaseField
    private String c_password;

    @DatabaseField
    private String gender;

    @DatabaseField
    private String imagepath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getC_password() {
        return c_password;
    }

    public void setC_password(String c_password) {
        this.c_password = c_password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
}
