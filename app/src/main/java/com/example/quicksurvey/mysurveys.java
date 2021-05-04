package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class mysurveys extends AppCompatActivity {

    String userid;
    String usertype;
    ListView livesurveys;
    ListView previous;
    ArrayList<String>live;
    ArrayList<String>hosted;
    ArrayAdapter adapter;
    ArrayAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysurveys);

        final Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        usertype = intent.getStringExtra("usertype");

        livesurveys = (ListView)findViewById(R.id.livesurveys);
        previous = (ListView)findViewById(R.id.hostedsurvey);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(mysurveys.this);
        databaseAccess.open();

        String timeStamp = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        Cursor cursor = databaseAccess.getLiveSurveys(userid, timeStamp);

        live = new ArrayList<>();
        if(cursor!=null && cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                int surv_id = Integer.parseInt(cursor.getString(
                        cursor.getColumnIndex("Survey_ID")));

                live.add(Integer.toString(surv_id));

            }
        }

        if(live.size()==0)
        {
            live.add("No surveys running");
        }
        adapter = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1, live);

        livesurveys.setAdapter(adapter);

        Cursor cursor2 = databaseAccess.getPastSurveys(userid, timeStamp);
        hosted = new ArrayList<>();
        if(cursor2!=null && cursor2.getCount()>0)
        {
            while (cursor2.moveToNext())
            {
                int surv_id = Integer.parseInt(cursor2.getString(
                        cursor2.getColumnIndex("Survey_ID")));

                hosted.add(Integer.toString(surv_id));

            }
        }

        if(hosted.size() == 0)
        {
            hosted.add("No surveys created");
        }

        adapter2 = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1, hosted);

        previous.setAdapter(adapter2);


        livesurveys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int surv_id = Integer.parseInt(livesurveys.getItemAtPosition(position).toString());
                Intent intent1 = new Intent(mysurveys.this, seeresults.class);
                intent1.putExtra("surveyid", surv_id);
                intent1.putExtra("userid", userid);
                intent1.putExtra("usertype", usertype);
                startActivity(intent1);
            }
        });

        previous.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int surv_id = Integer.parseInt(livesurveys.getItemAtPosition(position).toString());
                Intent intent1 = new Intent(mysurveys.this, seeresults.class);
                intent1.putExtra("surveyid", surv_id);
                intent1.putExtra("userid", userid);
                intent1.putExtra("usertype", usertype);
                startActivity(intent1);
            }
        });

    }


}
