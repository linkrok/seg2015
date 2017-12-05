package com.seg2015.e_prokop.chlores2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.seg2015.e_prokop.chlores2.R;

public class Calendar extends AppCompatActivity {

    public CalendarView calendarView;
    private static String strDate;
    private String calDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_page);

        calendarView = (CalendarView) findViewById(R.id.calendar);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
                strDate = 1+ month + "/" + dayOfMonth + "/" + year;
                calDate = strDate;
            }
        });

        Button saveButton = (Button) findViewById(R.id.save_date);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateChore.updateCalendarDateText(calDate);
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        this.finish();
    }

    public static String getDate(){
        return strDate;
    }



}
