package com.seg2015.e_prokop.chlores2.activity;

import java.util.ArrayList;
import java.util.List;

public class Chore {
    private String name;
    private int reward;
    private String description;
    private String date;
    private List<String> userList;

    public Chore(String name, int reward, String description,String date){
        this.name = name;
        this.reward = reward;
        this.description = description;
        this.date = date;
        this.userList = new ArrayList<String>();
    }

    public String getName(){
        return name;
    }

    public int getReward(){
        return reward;
    }

    public String getDescription(){
        return description;
    }

    public String getDate(){
        return date;
    }

    public List<String> getAllUsers(){
        return userList;
    }

    public void setName(String newName){
        name = newName;
    }

    public void setReward(int newReward){
        reward = newReward;
    }

    public void setDescription(String newDescritption){
        description = newDescritption;
    }

    public void setDate(String newDate){
        date = newDate;

    }

    public void addUser(String newUser){
        userList.add(newUser);
    }
}
