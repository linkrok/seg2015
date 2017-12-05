package com.seg2015.e_prokop.chlores2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.seg2015.e_prokop.chlores2.R;


public class Reward extends AppCompatActivity {

    private ListView choreList;
    private SimpleCursorAdapter choreTitleAdapter;
    private DatabaseHandler dbHandle;
    private TextView rewardText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rewards_layout);

        dbHandle = new DatabaseHandler(this);

        choreList = (ListView) findViewById(R.id.chore_active_list);
        rewardText = (TextView) findViewById(R.id.reward_amount);

        int rewardNum = dbHandle.getUsersRewardAmount();
        rewardText.setText(""+rewardNum);

        updateMenu();

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
                Intent intent = new Intent(Reward.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.my_chores:
                Intent intent2 = new Intent(Reward.this, MyChore.class);
                startActivity(intent2);
                break;
            case R.id.create_chore:
                Intent intent3 = new Intent(Reward.this, CreateChore.class);
                startActivity(intent3);
                break;
            case R.id.my_rewards:
                Intent intent4 = new Intent(Reward.this, Reward.class);
                startActivity(intent4);
                break;
            case R.id.log_out:
                Intent intent5 = new Intent(Reward.this, Login.class);
                startActivity(intent5);
                finish();
                break;
            default:
                Toast.makeText(Reward.this, "Already Here!", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    public void updateMenu(){
        choreTitleAdapter = new SimpleCursorAdapter(this, R.layout.single_chore,dbHandle.getMyRewards(),new String[] { "Name","Reward","Description","Date" },
                new int[] {R.id.single_chore_name,R.id.single_chore_reward,R.id.single_chore_description, R.id.single_chore_date });
        choreList.setAdapter(choreTitleAdapter);
    }

}