package com.example.quicksurvey;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class User extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    ListView surveyAvailable;
    ListView surveygrp;
    ListView surveydept;
    ListView surveyorg;
    ArrayList<String> surveys;
    ArrayList<String> surveys2;
    ArrayList<String> surveys3;
    ArrayList<String> surveys4;
    ArrayAdapter adapter;
    ArrayAdapter adapter2;
    ArrayAdapter adapter3;
    ArrayAdapter adapter4;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("userid");
        Toast.makeText(this, user_id, Toast.LENGTH_SHORT).show();
        surveyAvailable = (ListView)findViewById(R.id.surveyAvailable);
        surveygrp = (ListView)findViewById(R.id.surveygrp);
        surveydept = (ListView)findViewById(R.id.surveydept);
        surveyorg = (ListView)findViewById(R.id.surveyorg);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.mysurveys:
                        Intent intent1 = new Intent(User.this, mysurveys.class);
                        intent1.putExtra("usertype", "user");
                        intent1.putExtra("userid", user_id);
                        startActivity(intent1);
                        return true;
                    case R.id.atsurveys:
                        Intent intent = new Intent(User.this, pastSurveys.class);
                        intent.putExtra("usertype", "user");
                        intent.putExtra("userid", user_id);
                        startActivity(intent);
                        return true;
                    case R.id.calendar:
                        Intent intent3 = new Intent(User.this, calendar.class);
                        intent3.putExtra("usertype", "user");
                        intent3.putExtra("userid", user_id);
                        startActivity(intent3);
                        return true;
                    case R.id.settings:
                        Intent intent2 = new Intent(User.this, settings.class);
                        intent2.putExtra("usertype", "user");
                        intent2.putExtra("userid", user_id);
                        startActivity(intent2);
                        return true;
                    case R.id.logout:
                        finish();
                        return true;
                    default:
                        return false;
                }

            }
        });
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(User.this);
        databaseAccess.open();

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        Cursor cursor = databaseAccess.getSurvforUser(user_id, timeStamp);
        surveys = new ArrayList<String>();

        if(cursor!=null && cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                int surv_id = Integer.parseInt(cursor.getString(
                        cursor.getColumnIndex("Survey_ID")));

                if(databaseAccess.getRespCount(surv_id,user_id)==0)
                {
                    surveys.add(Integer.toString(surv_id));
                }

            }

            if(surveys.size()==0)
            {
                surveys.add("No survey available");
            }
            adapter = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_list_item_1, surveys);

            surveyAvailable.setAdapter(adapter);
        }
        else{
            if(surveys.size()==0)
            {
                surveys.add("No survey available");
            }
            adapter = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_list_item_1, surveys);

            surveyAvailable.setAdapter(adapter);
        }

        surveys2 = new ArrayList<>();
        Cursor cursor1 = databaseAccess.getGrpfromUser(user_id);
        if(cursor1!=null && cursor1.getCount()>0)
        {
            Cursor cursor2;
            while (cursor1.moveToNext())
            {
                String grp = cursor1.getString(cursor1.getColumnIndex("Group_ID"));
                cursor2 = databaseAccess.getSurvfromGrp(grp);
                if(cursor2!=null && cursor2.getCount()>0)
                {
                    int surv_id = Integer.parseInt(cursor2.getString(
                            cursor2.getColumnIndex("Survey_ID")));

                    if(databaseAccess.getRespCount(surv_id,user_id)==0)
                    {
                        surveys2.add(Integer.toString(surv_id));
                    }

                }
            }

            if(surveys2.size()==0)
            {
                surveys2.add("No survey available");
            }
            adapter2 = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_list_item_1, surveys2);

            surveygrp.setAdapter(adapter2);
        }
        else{
            if(surveys2.size()==0)
            {
                surveys2.add("No survey available");
            }
            adapter2 = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_list_item_1, surveys2);

            surveygrp.setAdapter(adapter2);
        }

        surveys3 = new ArrayList<>();

        String dept = databaseAccess.getDeptfromUser(user_id);
        Cursor cursor2 = databaseAccess.getSurvfromDept(dept);
        if(cursor2!=null && cursor2.getCount()>0)
        {
            while (cursor2.moveToNext())
            {
                int surv_id = Integer.parseInt(cursor2.getString(
                        cursor2.getColumnIndex("Survey_ID")));

                if(databaseAccess.getRespCount(surv_id,user_id)==0)
                {
                    surveys3.add(Integer.toString(surv_id));
                }
            }

            if(surveys3.size()==0)
            {
                surveys3.add("No survey available");
            }

            adapter3 = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_list_item_1, surveys3);
            surveydept.setAdapter(adapter3);
        }
        else{
            if(surveys3.size()==0)
            {
                surveys3.add("No survey available");
            }

            adapter3 = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_list_item_1, surveys3);
            surveydept.setAdapter(adapter3);
        }

        surveys4 = new ArrayList<>();
        Cursor cursor3 = databaseAccess.getSurvfromOrg();
        if(cursor3!=null && cursor3.getCount()>0)
        {
            while (cursor3.moveToNext())
            {

                int surv_id = Integer.parseInt(cursor3.getString(
                        cursor3.getColumnIndex("Survey_ID")));

                if(databaseAccess.getRespCount(surv_id,user_id)==0)
                {
                    surveys4.add(Integer.toString(surv_id));
                }
            }

            if(surveys4.size()==0)
            {
                surveys4.add("No survey available");
            }

            adapter4 = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_list_item_1, surveys4);

            surveyorg.setAdapter(adapter4);
        }
        else {
            if(surveys4.size()==0)
            {
                surveys4.add("No survey available");
            }

            adapter4 = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_list_item_1, surveys4);

            surveyorg.setAdapter(adapter4);
        }


        databaseAccess.close();


        surveyAvailable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int text = Integer.parseInt(surveyAvailable.getItemAtPosition(position).toString());
                Intent intent = new Intent(User.this, attemptSurvey.class);
                intent.putExtra("usertype", "user");
                intent.putExtra("userid", user_id);
                intent.putExtra("surveyid", text);
                startActivity(intent);

            }
        });

        surveygrp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int text = Integer.parseInt(surveygrp.getItemAtPosition(position).toString());
                Intent intent = new Intent(User.this, attemptSurvey.class);
                intent.putExtra("usertype", "user");
                intent.putExtra("userid", user_id);
                intent.putExtra("surveyid", text);
                startActivity(intent);
            }
        });

        surveydept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int text = Integer.parseInt(surveydept.getItemAtPosition(position).toString());
                Intent intent = new Intent(User.this, attemptSurvey.class);
                intent.putExtra("usertype", "user");
                intent.putExtra("userid", user_id);
                intent.putExtra("surveyid", text);
                startActivity(intent);
            }
        });

        surveyorg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int text = Integer.parseInt(surveyorg.getItemAtPosition(position).toString());
                Intent intent = new Intent(User.this, attemptSurvey.class);
                intent.putExtra("usertype", "user");
                intent.putExtra("userid", user_id);
                intent.putExtra("surveyid", text);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    public void addSurvey(View view)
    {
        Intent intent = new Intent(User.this, createSurvey.class);
        intent.putExtra("userid", user_id);
        intent.putExtra("usertype", "user");


        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(User.this);
        databaseAccess.open();
        int survid = databaseAccess.getMaxSurv();
        databaseAccess.close();
        intent.putExtra("surveyid", Integer.toString(survid));
        startActivity(intent);
    }
}
