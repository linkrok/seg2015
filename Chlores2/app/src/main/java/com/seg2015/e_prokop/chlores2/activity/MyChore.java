package com.seg2015.e_prokop.chlores2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.seg2015.e_prokop.chlores2.R;

public class MyChore extends AppCompatActivity {

    private ListView choreList;
    private SimpleCursorAdapter choreTitleAdapter;
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
                Intent intent = new Intent(MyChore.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.create_chore:
                Intent intent3 = new Intent(MyChore.this, CreateChore.class);
                startActivity(intent3);
                break;
            case R.id.my_rewards:
                Intent intent4 = new Intent(MyChore.this, Reward.class);
                startActivity(intent4);
                break;
            case R.id.log_out:
                Intent intent5 = new Intent(MyChore.this, Login.class);
                startActivity(intent5);
                finish();
                break;
            default:
                Toast.makeText(MyChore.this, "Already Here!", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    public void callTextViewOptions(View view, final String selectedOption){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Have you completed the selected chore?")
                .setPositiveButton("NOPE!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("Finished!",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHandle.putInHistory(selectedOption);
                        updateMenu();
                    }
                })
                .create();
        dialog.show();

    }

    public void updateMenu(){
        choreTitleAdapter = new SimpleCursorAdapter(this, R.layout.single_chore,dbHandle.getMyChoresList(),new String[] { "Name","Reward","Description","Date" },
                new int[] {R.id.single_chore_name,R.id.single_chore_reward,R.id.single_chore_description, R.id.single_chore_date });
        choreList.setAdapter(choreTitleAdapter);

    }

}
