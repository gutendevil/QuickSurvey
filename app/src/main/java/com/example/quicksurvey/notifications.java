package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class notifications extends AppCompatActivity {

    Cursor cursor;
    ListView listView;
    ArrayList<String>list;
    ArrayAdapter adapter;
    String userid;
    String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        usertype = intent.getStringExtra("usertype");

        listView = (ListView)findViewById(R.id.notify_list);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(notifications.this);
        databaseAccess.open();
        cursor = databaseAccess.getNotfications();

        list = new ArrayList<String>();
        if(cursor!=null && cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                list.add(cursor.getString(cursor.getColumnIndex("Survey_ID")));

            }

            adapter = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);
        }
        else{
            Toast.makeText(notifications.this, "No data to show", Toast.LENGTH_SHORT).show();
        }

        databaseAccess.close();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = listView.getItemAtPosition(position).toString();
                Intent intent = new Intent(notifications.this, notifications2.class);
                intent.putExtra("surveyid", text);
                DatabaseAccess databaseAccess1 = DatabaseAccess.getInstance(notifications.this);
                databaseAccess1.open();
                intent.putExtra("userid", userid);
                intent.putExtra("usertype", usertype);
                Log.i("error", text);
                String app = databaseAccess1.approval(Integer.parseInt(text));
                intent.putExtra("approval", app);
                startActivity(intent);
            }
        });

    }

}
