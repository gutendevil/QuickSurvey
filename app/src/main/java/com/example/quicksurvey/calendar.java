package com.example.quicksurvey;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class calendar extends AppCompatActivity {

    private CompactCalendarView calendarView;
    String userid;
    String usertype;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM-yyyy",
            Locale.getDefault());
    private SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);


        calendarView = (CompactCalendarView) findViewById(R.id.calendarView);
        calendarView.setUseThreeLetterAbbreviation(true);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        usertype = intent.getStringExtra("usertype");

        String timeStamp = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        String today = dateFormatMonth.format(Calendar.getInstance().getTime());
        actionBar.setTitle(today);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(calendar.this);
        databaseAccess.open();

        Cursor cursor = databaseAccess.getLiveSurveys2(userid, timeStamp);

        if(cursor!=null && cursor.getCount()>0)
        {
            while(cursor.moveToNext())
            {
                String date = cursor.getString(cursor.getColumnIndex("Deadline"));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formatter.setTimeZone(TimeZone.getTimeZone("IST"));

                try {
                    Date date2 = formatter.parse(date);
                    long time = date2.getTime();
                    Event event = new Event(Color.GREEN, time);
                    calendarView.addEvent(event);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        }

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                String deadline = sdf.format(dateClicked);

                Intent intent1 = new Intent(calendar.this, todaysurveys.class);
                intent1.putExtra("userid", userid);
                intent1.putExtra("usertype", usertype);
                intent1.putExtra("deadline", deadline);
                startActivity(intent1);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });




    }
}
