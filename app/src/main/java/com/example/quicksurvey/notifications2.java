package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class notifications2 extends AppCompatActivity {

    int survid;
    String approval;
    TextView request;
    String userid;
    String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications2);

        Intent intent = getIntent();
        survid = Integer.parseInt(intent.getStringExtra("surveyid"));
        userid = intent.getStringExtra("userid");
        usertype = intent.getStringExtra("usertype");
        approval = intent.getStringExtra("approval");
        request = (TextView)findViewById(R.id.request);
        if(approval.equals("pending"))
        {
            request.setText("Would you like to approve the survey?");
        }
        else{
            request.setText("Would you like to cancel the survey?");
        }

    }

    public void approvesurvey(View view)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(notifications2.this);
        databaseAccess.open();
        if(approval.equals("pending"))
        {
            databaseAccess.getApproval(survid);
        }
        else{
            databaseAccess.getCancel(survid);
        }

        databaseAccess.close();
        Intent intent = new Intent(notifications2.this, Admin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void cancelsurvey(View view)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(notifications2.this);
        databaseAccess.open();
        if(approval.equals("pending"))
        {
            databaseAccess.getCancel(survid);
        }

        databaseAccess.close();
        Intent intent = new Intent(notifications2.this, Admin.class);
        intent.putExtra("userid", userid);
        intent.putExtra("usertype", usertype);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
