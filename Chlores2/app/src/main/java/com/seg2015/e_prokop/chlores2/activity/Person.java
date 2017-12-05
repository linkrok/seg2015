package com.seg2015.e_prokop.chlores2.activity;


public class Person {
    private String username, role;
    private int password, rewardAmount;

    public Person(String username, String role, int password){
        this.username = username;
        this.role = role;
        this.password = password;
        this.rewardAmount = 0;
    }

    public String getUsername(){
        return username;
    }
    public String getRole(){
        return role;
    }
    public int getPassword(){
        return password;
    }
    public int getRewardAmount(){
        return rewardAmount;
    }

    public void setUsername(String newUserName){
        username = newUserName;
    }
    public void setRole(String newRole){
        role = newRole;
    }
    public void setPassword(int newPassword){
        password = newPassword;
    }
    public void setRewardAmount(int newAmount){
        rewardAmount = newAmount;
    }
}
