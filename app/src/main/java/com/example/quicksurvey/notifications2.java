package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class notifications2 extends AppCompatActivity {

    int survid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications2);

        Intent intent = getIntent();
        survid = Integer.parseInt(intent.getStringExtra("surveyid"));

    }

    public void approvesurvey(View view)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(notifications2.this);
        databaseAccess.open();
        databaseAccess.getApproval(survid);
        databaseAccess.close();
        Intent intent = new Intent(notifications2.this, Admin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void cancelsurvey(View view)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(notifications2.this);
        databaseAccess.open();
        databaseAccess.getCancel(survid);
        databaseAccess.close();
        Intent intent = new Intent(notifications2.this, Admin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
