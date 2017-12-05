package com.seg2015.e_prokop.chlores2.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chlores.db";
    private static final String TABLE_ACCOUNT = "account";
    private static final String TABLE_NEW_CHORES = "availableChores";
    private static final String TABLE_CHORE_HISTORY = "choreHistory";
    private static final String TABLE_PERSONAL_CHORE = "personalChore";
    private static final String ACCOUNT_COLUMN_ID = "_id";
    private static final String NEW_CHORE_COLUMN_ID = "_id";
    private static final String HISTORY_CHORE_COLUMN_ID = "_id";
    private static final String COLUMN_ID = "_id";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ACCOUNT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ACCOUNT + "("
                + ACCOUNT_COLUMN_ID + " INTEGER PRIMARY KEY, 'Username' TEXT, 'Role' TEXT, 'Password' INTEGER, 'RewardAmount' INTEGER)";
        db.execSQL(CREATE_ACCOUNT_TABLE);

        String CREATE_CHLORES_TABLE  = "CREATE TABLE IF NOT EXISTS "+ TABLE_NEW_CHORES + "("
                +NEW_CHORE_COLUMN_ID + " INTEGER PRIMARY KEY, 'Name' TEXT, 'Reward' INTEGER, 'Description' TEXT, 'Date' TEXT)";

        db.execSQL(CREATE_CHLORES_TABLE);

        String CREATE_HISTORY_TABLE  = "CREATE TABLE IF NOT EXISTS "+ TABLE_CHORE_HISTORY + "("
                + HISTORY_CHORE_COLUMN_ID + " INTEGER PRIMARY KEY, 'Username' TEXT,'Name' TEXT, 'Reward' INTEGER, 'Description' TEXT,'Date' TEXT)";

        db.execSQL(CREATE_HISTORY_TABLE);

        String CREATE_PERSONAL_TABLE  = "CREATE TABLE IF NOT EXISTS "+ TABLE_PERSONAL_CHORE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, 'Username' TEXT, 'Name' TEXT, 'Reward' INTEGER, 'Description' TEXT,'Date' TEXT)";

        db.execSQL(CREATE_PERSONAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }

    public void addChore(Chore chore){
        ContentValues choreData = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        choreData.put("Name",chore.getName());
        choreData.put("Reward",chore.getReward());
        choreData.put("Description",chore.getDescription());
        choreData.put("Date",chore.getDate());
        db.insert(TABLE_NEW_CHORES, null, choreData);

        db.close();
    }

    public void putInMyChores(String selected){
        String sqlSelect = "SELECT * FROM "+TABLE_NEW_CHORES+" WHERE Name= '"+ selected+"'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sqlSelect, null);

        if(cursor.moveToFirst()){
            ContentValues choreData = new ContentValues();
            choreData.put("Username", MainActivity.currentUser.getUsername());
            choreData.put("Name",cursor.getString(cursor.getColumnIndex("Name")));
            choreData.put("Reward",cursor.getString(cursor.getColumnIndex("Reward")));
            choreData.put("Description",cursor.getString(cursor.getColumnIndex("Description")));
            choreData.put("Date",cursor.getString(cursor.getColumnIndex("Date")));
            db.insert(TABLE_PERSONAL_CHORE, null, choreData);

        }
        db.delete(TABLE_NEW_CHORES, "Name='"+selected+"'",null);
        cursor.close();
        db.close();

    }
    public void assignInMyChores(Chore chore, String user){
        ContentValues choreData = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println(MainActivity.currentUser.getUsername()+ " " + chore.getName());
        choreData.put("Username",user);
        choreData.put("Name",chore.getName());
        choreData.put("Reward",chore.getReward());
        choreData.put("Description",chore.getDescription());
        choreData.put("Date",chore.getDate());
        db.insert(TABLE_PERSONAL_CHORE, null, choreData);

        db.close();

    }

    public void putInHistory(String selected){
        String sqlSelect = "SELECT * FROM "+TABLE_PERSONAL_CHORE+" WHERE Name= '"+ selected+"'";
        System.out.println(sqlSelect);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sqlSelect, null);

        if(cursor.moveToFirst()){
            ContentValues choreData = new ContentValues();
            choreData.put("Username", MainActivity.currentUser.getUsername());
            choreData.put("Name",cursor.getString(cursor.getColumnIndex("Name")));
            System.out.println(cursor.getString(cursor.getColumnIndex("Reward")));
            calculateTotalReward(MainActivity.currentUser.getUsername(), cursor.getString(cursor.getColumnIndex("Reward")));
            choreData.put("Reward",cursor.getString(cursor.getColumnIndex("Reward")));
            choreData.put("Description",cursor.getString(cursor.getColumnIndex("Description")));
            choreData.put("Date",cursor.getString(cursor.getColumnIndex("Date")));
            db.insert(TABLE_CHORE_HISTORY, null, choreData);

        }
        db.delete(TABLE_PERSONAL_CHORE, "Name='"+selected+"'",null);
        cursor.close();
        db.close();


    }


    public boolean[] findUser(String name, int password){
        boolean[] flags = {false, false, false};
        String sqlUser = "Select * FROM " + TABLE_ACCOUNT + " WHERE Username=\"" +
                name + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sqlUser, null);

        if(cursor.moveToFirst()){
            int idPassword = cursor.getColumnIndex("Password");
            int idRole = cursor.getColumnIndex("Role");
            int storedPass = Integer.parseInt(cursor.getString(idPassword));
            if(password == storedPass){
                flags[1]=true;
            }
            if(cursor.getString(idRole).equals("Parent") || cursor.getString(idRole).equals("Roommate")){
                flags[2]=true;
            }
            flags[0] = true;
        }
        db.close();
        return flags;
    }


    public void createUser(Person person){
        ContentValues userData = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        userData.put("Username",person.getUsername());
        userData.put("Role",person.getRole());
        userData.put("Password",person.getPassword());
        userData.put("RewardAmount", 0);

        db.insert(TABLE_ACCOUNT, null, userData);

        db.close();
    }

    public ArrayList<String> getAllUsers(){

        ArrayList<String> userNames = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        userNames.add("");
        Cursor cursor = db.query(TABLE_ACCOUNT,new String[]{"_id","Username"},null, null, null,null, null);

        while(cursor.moveToNext()){
            int id = cursor.getColumnIndex("Username");
            System.out.println(cursor.getString(id));
            userNames.add(cursor.getString(id));
        }
        cursor.close();
        db.close();
        return userNames;

    }
    private void calculateTotalReward(String user, String reward){
        String sqlUser = "Select * FROM " + TABLE_ACCOUNT + " WHERE Username=\"" +
                user + "\"";

        int newReward = Integer.parseInt(reward);
        ContentValues rewardData = new ContentValues();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sqlUser, null);

        if(cursor.moveToFirst()){
            int id = cursor.getColumnIndex("_id");
            int idR = cursor.getColumnIndex("RewardAmount");
            int currentPoints = cursor.getInt(idR);
            currentPoints += newReward;
            rewardData.put("RewardAmount",currentPoints);
            db.update(TABLE_ACCOUNT, rewardData, "_id=" + cursor.getString(id), null);
        }
    }

    public int getUsersRewardAmount(){
        int totalAmount = 0;
        String user = MainActivity.currentUser.getUsername();
        String sqlReward = "Select * FROM " + TABLE_ACCOUNT + " WHERE Username=\"" +
                user + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sqlReward, null);

        if(cursor.moveToFirst()){
            int idR = cursor.getColumnIndex("RewardAmount");
            totalAmount = cursor.getInt(idR);
        }

        return totalAmount;
    }

    public void deleteTableRow(String selected){
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println(selected);
        db.delete(TABLE_NEW_CHORES, "Name='"+selected+"'",null);
        db.close();
    }

    public Cursor getNewChoresList(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NEW_CHORES,new String[]{"_id","Name","Reward", "Description", "Date"},null, null, null,null, null);

        return cursor;
    }

    public Cursor getMyChoresList(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PERSONAL_CHORE,new String[]{"_id","Name","Reward", "Description", "Date"},null, null, null,null, null);

        return cursor;
    }

    public Cursor getMyRewards(){

        String user = MainActivity.currentUser.getUsername();
        String sqlReward = "Select * FROM " + TABLE_CHORE_HISTORY + " WHERE Username=\"" +
                user + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sqlReward, null);

        return cursor;
    }

    public String[] getChoreInfo(String name){
        String[] info = new String[4];
        String sqlChore = "Select * FROM " + TABLE_NEW_CHORES + " WHERE Name=\"" +
                name + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sqlChore, null);

        if(cursor.moveToFirst()){
            int idN = cursor.getColumnIndex("Name");
            int idR = cursor.getColumnIndex("Reward");
            int idD = cursor.getColumnIndex("Description");
            int idDate = cursor.getColumnIndex("Date");

            info[0]= cursor.getString(idN);
            info[1]= cursor.getString(idR);
            info[2]= cursor.getString(idD);
            info[3]= cursor.getString(idDate);

        }
        return info;
    }


}
