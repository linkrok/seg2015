package com.seg2015.e_prokop.chlores2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.seg2015.e_prokop.chlores2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e_prokop on 2017-11-30.
 */

public class EditChore extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DatabaseHandler dbHandle;
    private int reward;
    private String[] assingedNames = new String[3];
    private List<Spinner> spinnerList;
    private boolean assignedFlag = false;
    private static TextView calendar;
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    public static String choreName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_chore_layout);

        dbHandle = new DatabaseHandler(this);

        populateLayout();

        ArrayList<String> usernames = dbHandle.getAllUsers();

        spinnerList = new ArrayList<Spinner>();

        spinner1 = (Spinner) findViewById(R.id.user1);
        spinner2 = (Spinner) findViewById(R.id.user2);
        spinner3 = (Spinner) findViewById(R.id.user3);

        ArrayAdapter<String> spunner1 =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, usernames);
        ArrayAdapter<String> spunner2 =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, usernames);
        ArrayAdapter<String> spunner3 =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, usernames);

        spunner1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spunner2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spunner3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(spunner1);
        spinner2.setAdapter(spunner2);
        spinner3.setAdapter(spunner3);

        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        spinner3.setOnItemSelectedListener(this);

        spinnerList.add(spinner1);
        spinnerList.add(spinner2);
        spinnerList.add(spinner3);

        calendar = (TextView) findViewById(R.id.edit_calendar_view);

        Button editChore = (Button) findViewById(R.id.edit_new_chore);

        editChore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.edit_chore_name);
                EditText rewardStr = (EditText) findViewById(R.id.edit_chore_reward);
                EditText description = (EditText) findViewById(R.id.edit_chore_description);

                if (rewardStr.getText().toString().length() > 0 ){
                    reward = Integer.parseInt(rewardStr.getText().toString());
                }else{
                    reward = 0;
                }

                Chore newChore = new Chore(name.getText().toString(), reward,
                        description.getText().toString(),CalendarForEdit.getDate());

                dbHandle.deleteTableRow(name.getText().toString());

                newChore(newChore);

                Intent intent = new Intent(EditChore.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void newChore(Chore chore){
        DatabaseHandler dbHandle = new DatabaseHandler(this);
        for (int i = 0; i < assingedNames.length; i++){
            if(assingedNames[i].length() > 0){
                assignedFlag = true;
                dbHandle.assignInMyChores(chore, assingedNames[i]);
            }
        }
        if (!assignedFlag){
            dbHandle.addChore(chore);
        }

    };

    public void openCalendar(View view){
        Intent intent = new Intent(EditChore.this, CalendarForEdit.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.parent_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.home:
                Intent intent = new Intent(EditChore.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.my_chores:
                Intent intent2 = new Intent(EditChore.this, MyChore.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.create_chore:
                Intent intent3 = new Intent(EditChore.this, CreateChore.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.my_rewards:
                Intent intent4 = new Intent(EditChore.this, Reward.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.log_out:
                Intent intent5 = new Intent(EditChore.this, Login.class);
                startActivity(intent5);
                finish();
                break;
            default:
                Toast.makeText(EditChore.this, "Already Here!", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String username = parent.getSelectedItem().toString();

        for (int i = 0; i < spinnerList.size(); i++){

            if (spinnerList.get(i).getId()==parent.getId()){
                assingedNames[i] = username;
            }
        }
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

    private void populateLayout(){
        String[] choreInfo = dbHandle.getChoreInfo(choreName);

        EditText name = (EditText) findViewById(R.id.edit_chore_name);
        EditText rewardStr = (EditText) findViewById(R.id.edit_chore_reward);
        EditText description = (EditText) findViewById(R.id.edit_chore_description);
        TextView calendar = (TextView) findViewById(R.id.edit_calendar_view);

        name.setText(choreInfo[0]);
        rewardStr.setText(choreInfo[1]);
        description.setText(choreInfo[2]);
        calendar.setText(choreInfo[3]);

        dbHandle.deleteTableRow(choreInfo[0]);

    }
    public static void updateCalendarDateText(String date){
        calendar.setText(date);
    }

    public static void setChoreName(String name){
        choreName = name;
    }
}

