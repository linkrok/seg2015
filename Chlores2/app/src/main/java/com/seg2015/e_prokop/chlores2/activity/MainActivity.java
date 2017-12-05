package com.seg2015.e_prokop.chlores2.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.seg2015.e_prokop.chlores2.R;


public class MainActivity extends AppCompatActivity {

    public DrawerLayout navigationMenu;
    public ActionBarDrawerToggle toggle;
    public NavigationView navigationView;
    private ListView choreList;
    private SimpleCursorAdapter choreTitleAdapter;
    private DatabaseHandler dbHandle;
    public static Person currentUser;

    public MainActivity(){
        if (Login.getUserPermission() != null){
            this.currentUser = Login.getUserPermission();
        }
        if (CreateAccount.person != null){
            this.currentUser = CreateAccount.person;
        }
        System.out.println(currentUser.getUsername());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandle = new DatabaseHandler(this);;
        choreList = (ListView) findViewById(R.id.chore_active_list);

        updateMenu();

        choreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.single_chore_name);
                String selected = tv.getText().toString();
                callTextViewOptions(view, selected);

            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu){

        if (currentUser.getRole().equals("Parent")){
            getMenuInflater().inflate(R.menu.parent_main_menu, menu);
        }
        if (currentUser.getRole().equals("Child")){
            getMenuInflater().inflate(R.menu.child_main_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.my_chores:
                Intent intent2 = new Intent(MainActivity.this, MyChore.class);
                startActivity(intent2);
                break;
            case R.id.create_chore:
                Intent intent3 = new Intent(MainActivity.this, CreateChore.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.my_rewards:
                Intent intent4 = new Intent(MainActivity.this, Reward.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.log_out:
                Intent intent5 = new Intent(MainActivity.this, Login.class);
                startActivity(intent5);
                finish();
                break;
            default:
                Toast.makeText(MainActivity.this, "Already Here!", Toast.LENGTH_SHORT).show();
                break;
        }


        return super.onOptionsItemSelected(item);

    }
    public void callTextViewOptions(View view, final String selectedName){
        final Dialog customDialog = new Dialog(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        if((currentUser.getRole().equals("Parent"))){
            customDialog.setContentView(R.layout.custom_parent_dialog);
        }
        if((currentUser.getRole().equals("Child"))){
            customDialog.setContentView(R.layout.custom_child_dialog);
        }
        Button acceptButton = (Button) customDialog.findViewById(R.id.accept);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandle.putInMyChores(selectedName);
                updateMenu();
                customDialog.dismiss();
            }
        });

        Button cancelButton = (Button) customDialog.findViewById(R.id.cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });

        Button deleteButton = (Button) customDialog.findViewById(R.id.delete);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandle.deleteTableRow(selectedName);
                updateMenu();
                customDialog.dismiss();
            }
        });

        Button editButton = (Button) customDialog.findViewById(R.id.edit);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditChore.class);
                EditChore.setChoreName(selectedName);
                startActivity(intent);
                customDialog.dismiss();
            }
        });

        customDialog.show();
    }

    public void updateMenu(){
        choreTitleAdapter = new SimpleCursorAdapter(this, R.layout.single_chore,dbHandle.getNewChoresList(),new String[] { "Name","Reward","Description","Date" },
                new int[] {R.id.single_chore_name,R.id.single_chore_reward,R.id.single_chore_description, R.id.single_chore_date });
        choreList.setAdapter(choreTitleAdapter);

    }

}
