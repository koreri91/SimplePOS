package com.app.estockcard.model;

public class User {
    private String id;
    private String pwd;
    private String username;
    private String dateCreated;
    private int level;

    private boolean isActive;

    private boolean isGranted;


    public final static int TESTER_LEVEL =1;
    public final static int USER_LEVEL =2;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setGranted(boolean granted) {
        isGranted = granted;
    }

    public boolean isGranted() {
        return isGranted;
    }
}
