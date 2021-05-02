package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class pastSurveys extends AppCompatActivity {

    ListView listView;
    ArrayList<String> surveys;
    ArrayAdapter adapter;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_surveys);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("userid");
        Toast.makeText(this, user_id, Toast.LENGTH_SHORT).show();

        listView = (ListView)findViewById(R.id.atsurveylist);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(pastSurveys.this);
        databaseAccess.open();
        Cursor cursor = databaseAccess.getSurvfromResp(user_id);

        surveys = new ArrayList<>();

        if(cursor!=null && cursor.getCount()>0)
        {
            while(cursor.moveToNext())
            {
                surveys.add(cursor.getString(cursor.getColumnIndex("Survey_ID")));
            }

            adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, surveys);
            listView.setAdapter(adapter);
        }
        else{
            Toast.makeText(this, "No Past surveys", Toast.LENGTH_SHORT).show();
        }
        databaseAccess.close();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(pastSurveys.this, listView.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
