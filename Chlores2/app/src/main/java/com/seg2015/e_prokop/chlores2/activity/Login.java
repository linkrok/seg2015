package com.seg2015.e_prokop.chlores2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.seg2015.e_prokop.chlores2.R;



public class Login extends AppCompatActivity {
    private DatabaseHandler dbHandle;
    public static Person user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);


        dbHandle = new DatabaseHandler(this);

        Button loginButton = (Button) findViewById(R.id.loginBut);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = (EditText) findViewById(R.id.username_field);
                EditText passwordStr = (EditText) findViewById(R.id.password_field);
                int password = Integer.parseInt(passwordStr.getText().toString());

                checkUsersExist(username.getText().toString(), password);

            }
        });

        Button loginButton2 = (Button) findViewById(R.id.newUser);

        loginButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, CreateAccount.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void checkUsersExist(String username, int password){
        boolean[] flags = dbHandle.findUser(username, password);
        System.out.println(flags[2] + " " + username);
        if(flags[0]){
            if(flags[1]){
                if(flags[2]){
                    user = new Person(username,"Parent",password);
                } else {
                    user = new Person(username,"Child",password);
                }
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Wrong password. Please try agaim.")
                        .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                dialog.show();
            }
        }else{
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Username doesn't exist. Try the options below.")
                    .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton("Create New Account", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Login.this, CreateAccount.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .create();
            dialog.show();
        }
    }
    public static Person getUserPermission(){
        return user;
    }
}


