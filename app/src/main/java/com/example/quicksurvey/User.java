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

import java.util.ArrayList;

public class User extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    ListView surveyAvailable;
    ArrayList<String> surveys;
    ArrayAdapter adapter;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("Profile");
        surveyAvailable = (ListView)findViewById(R.id.surveyAvailable);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.settings:
                        Intent intent = new Intent(User.this, settings.class);
                        startActivity(intent);
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

        Cursor cursor = databaseAccess.getSurvforUser(user_id);
        surveys = new ArrayList<String>();

        if(cursor!=null && cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                surveys.add(cursor.getString(cursor.getColumnIndex("Survey_ID")));
            }
            adapter = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_list_item_1, surveys);

            surveyAvailable.setAdapter(adapter);
        }
        else{
            Toast.makeText(this, "No surveys Available", Toast.LENGTH_SHORT).show();
        }
        databaseAccess.close();


        surveyAvailable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = surveyAvailable.getItemAtPosition(position).toString();
                Toast.makeText(User.this, text, Toast.LENGTH_SHORT).show();
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
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(User.this);
        databaseAccess.open();
        int survid = databaseAccess.getMaxSurv();
        databaseAccess.close();
        intent.putExtra("surveyid", Integer.toString(survid));
        startActivity(intent);
    }
}
