package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class submitTo extends AppCompatActivity {

    int survid;
    EditText survname;
    EditText deadline;
    EditText depname;
    EditText grpno;
    EditText user_id;
    RadioButton org;
    RadioButton dept;
    RadioButton grp;
    RadioButton indiv;
    RadioGroup rdgrp1;
    RadioGroup rdgrp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_to);

        Intent intent = getIntent();
        survid =  Integer.parseInt(intent.getStringExtra("surveyid"));
        survname = (EditText)findViewById(R.id.surveyname);
        deadline = (EditText)findViewById(R.id.deadline);
        depname = (EditText)findViewById(R.id.depname);
        grpno = (EditText)findViewById(R.id.grpno);
        user_id = (EditText)findViewById(R.id.user_id);
        org = (RadioButton)findViewById(R.id.org);
        dept = (RadioButton)findViewById(R.id.dep);
        grp = (RadioButton)findViewById(R.id.grp);
        indiv = (RadioButton)findViewById(R.id.indiv);
        rdgrp1 = (RadioGroup)findViewById(R.id.rdgrp1);
        rdgrp2 = (RadioGroup)findViewById(R.id.rdgrp2);

        rdgrp1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    grp.setChecked(false);
                    indiv.setChecked(false);
                }
            }
        });

        rdgrp2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    dept.setChecked(false);
                    org.setChecked(false);
                }
            }
        });



    }



    public void goToHome(View view)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(submitTo.this);
        databaseAccess.open();
        databaseAccess.insertSurvey(survid, survname.getText().toString(), deadline.getText().toString());
        if(org.isChecked()){
            databaseAccess.insertSurvinOrg(survid);
        }
        else if(dept.isChecked())
        {
            databaseAccess.insertSurvinDept(survid, depname.getText().toString());
        }
        else if(grp.isChecked())
        {
            databaseAccess.insertSurvinGrp(survid, grpno.getText().toString());
        }
        else if(indiv.isChecked()){
            databaseAccess.insertSurvinUser(survid, user_id.getText().toString());
        }
        databaseAccess.close();
        Intent intent = new Intent(submitTo.this, User.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
