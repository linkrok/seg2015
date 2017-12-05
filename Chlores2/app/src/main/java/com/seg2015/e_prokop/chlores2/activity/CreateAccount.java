package com.seg2015.e_prokop.chlores2.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.seg2015.e_prokop.chlores2.R;

/**
 * Created by e_prokop on 2017-11-24.
 */

public class CreateAccount extends AppCompatActivity implements OnItemSelectedListener {
    private String usersRole;
    public static Person person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        Spinner spinner = (Spinner) findViewById(R.id.role_combobox);

        ArrayAdapter<CharSequence> spunnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.role_choices, android.R.layout.simple_spinner_item);

        spunnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spunnerAdapter);
        spinner.setOnItemSelectedListener(this);

        Button createUser = (Button) findViewById(R.id.create_account);

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.new_username);
                EditText passwordStr = (EditText) findViewById(R.id.new_password);
                int password = Integer.parseInt(passwordStr.getText().toString());

                person = new Person(name.getText().toString(), usersRole, password);
                newUser(person);

                Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void newUser(Person person){
        DatabaseHandler dbHandle = new DatabaseHandler(this);

        dbHandle.createUser(person);
    };


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        usersRole = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Please Select Your Role!")
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        dialog.show();
    }
}
