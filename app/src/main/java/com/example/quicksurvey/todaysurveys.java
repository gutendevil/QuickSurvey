package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class todaysurveys extends AppCompatActivity {

    private ListView surveys;
    ArrayList<String> list;
    ArrayAdapter adapter;

    String userid;
    String usertype;
    String deadline;

    private SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todaysurveys);

        surveys = (ListView)findViewById(R.id.datesurveys);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        usertype = intent.getStringExtra("usertype");
        deadline = intent.getStringExtra("deadline");

        try {
            Date date = sdf.parse(deadline);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            Date dt = c.getTime();
            String deadline2 = sdf.format(dt);

            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(todaysurveys.this);
            databaseAccess.open();
            Cursor cursor = databaseAccess.getDateSurveys(deadline,deadline2);
            list = new ArrayList<>();
            if(cursor!=null && cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    String surv_id = cursor.getString(
                            cursor.getColumnIndex("Survey_ID"));

                    list.add(surv_id);

                }

                if(list.size() == 0)
                {
                    list.add("No surveys ending this date");

                }

                adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_list_item_1, list);

                surveys.setAdapter(adapter);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
