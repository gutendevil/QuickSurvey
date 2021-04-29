package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        Button submit = (Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String temp = username.getText().toString();
                System.out.println(temp);
                if(temp.equals("admin"))
                {

                    Intent intent = new Intent(getApplicationContext(), Admin.class);
                    intent.putExtra("Profile", username.getText().toString());
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), User.class);
                    intent.putExtra("Profile", username.getText().toString());
                    startActivity(intent);
                }

            }
        });
    }



}
