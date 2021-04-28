package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class createSurvey extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_survey);
    }

    public void submit(View view)
    {
        Intent intent = new Intent(createSurvey.this, submitTo.class);
        startActivity(intent);
    }
}
