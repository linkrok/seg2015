package com.seg2015.e_prokop.chlores2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.seg2015.e_prokop.chlores2.R;


public class ChoreHistory extends AppCompatActivity {

    public DrawerLayout navigationMenu;
    public ActionBarDrawerToggle toggle;
    public NavigationView navigationView;
    private ListView choreList;
    private ArrayAdapter choreTitleAdapter;
    private DatabaseHandler dbHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandle = new DatabaseHandler(this);
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
        if (MainActivity.currentUser.getRole().equals("Parent")){
            getMenuInflater().inflate(R.menu.parent_main_menu, menu);
        }
        if (MainActivity.currentUser.getRole().equals("Child")){
            getMenuInflater().inflate(R.menu.child_main_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.home:
                Intent intent = new Intent(ChoreHistory.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.my_chores:
                Intent intent2 = new Intent(ChoreHistory.this, MyChore.class);
                startActivity(intent2);
                break;
            case R.id.create_chore:
                Intent intent3 = new Intent(ChoreHistory.this, CreateChore.class);
                startActivity(intent3);
                break;
            case R.id.my_rewards:
                Intent intent4 = new Intent(ChoreHistory.this, Reward.class);
                startActivity(intent4);
                break;
            default:
                Toast.makeText(ChoreHistory.this, "Already Here!", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    public void callTextViewOptions(View view, String selected){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("What you want to do?")
                .setNegativeButton("View Info", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        dialog.show();

    }
    public void updateMenu(){
//        ArrayList<String[]> listOfNames = dbHandle.updateMainMenuList();
//        if(choreTitleAdapter==null){
//
//            choreTitleAdapter = new ArrayAdapter<>(this, R.layout.single_chore, R.id.single_chore_name, listOfNames);
//            choreList.setAdapter(choreTitleAdapter);
//        }
//        else{
//            choreTitleAdapter.clear();
//            choreTitleAdapter.addAll(listOfNames);
//            choreTitleAdapter.notifyDataSetChanged();
//        }
    }

}
