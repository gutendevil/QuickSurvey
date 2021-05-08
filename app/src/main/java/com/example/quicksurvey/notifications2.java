package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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
            String pass = databaseAccess.getPassword(userid);
            String rec_id = databaseAccess.getUserFromSurv(survid);
            String recname = databaseAccess.getName(rec_id);
            String recmail = databaseAccess.getEmail(rec_id);
            String usermail = databaseAccess.getEmail(userid);
            String subject = "Regarding Survey Apporval";
            StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().
                    permitAll().build();
            StrictMode.setThreadPolicy(threadPolicy);
            String message = "Dear "+recname+", \n Your Survey with ID-"+survid+" has been approved.";
            //System.out.println(usermail+" "+recmail+" "+pass);
            try {
                //System.out.println("Mail Sent Successfully");

                SendEmail.sendEmail(usermail, recmail, subject, message, pass);
                Log.i("Mail", "Sent successfully");
            }
            catch (Exception e)
            {
                //System.out.println("Exception");
                Log.i("Mail", "Exception");
                e.printStackTrace();
            }

            Cursor cursor = databaseAccess.ifSurvInUser(survid);
            Cursor cursor1 = databaseAccess.ifSurvinGrp(survid);
            Cursor cursor3 = databaseAccess.ifSurvinDept(survid);
            boolean inOrg = databaseAccess.ifSurvinOrg(survid);

            if(cursor != null && cursor.getCount()>0)
            {
                if(cursor.moveToFirst()) {
                    rec_id = cursor.getString(cursor.getColumnIndex("User_ID"));
                    recname = databaseAccess.getName(rec_id);
                    recmail = databaseAccess.getEmail(rec_id);

                    subject = "A new survey is opened for you";

                    message = "Dear " + recname + ", \n A new survey Survey No-" + survid + " has been" +
                            " opened for you ";

                    try {
                        //System.out.println("Mail Sent Successfully");

                        SendEmail.sendEmail(usermail, recmail, subject, message, pass);
                        Log.i("Mail", "Sent successfully");
                    } catch (Exception e) {
                        //System.out.println("Exception");
                        Log.i("Mail", "Exception");
                        e.printStackTrace();
                    }
                }
            }
            else if(cursor1!=null && cursor1.getCount()>0)
            {
                if(cursor1.moveToFirst())
                {
                    String grp_id = cursor1.getString(cursor1.getColumnIndex("Group_ID"));
                    Cursor cursor2 = databaseAccess.getUsersFromGrp(grp_id);

                    while(cursor2.moveToNext())
                    {
                        rec_id = cursor2.getString(cursor2.getColumnIndex("User_ID"));
                        recname = databaseAccess.getName(rec_id);
                        recmail = databaseAccess.getEmail(rec_id);

                        subject = "A new survey is opened for you";

                        message = "Dear " + recname + ", \n A new survey Survey No-" + survid + " has been" +
                                " opened for you ";

                        try {
                            //System.out.println("Mail Sent Successfully");

                            SendEmail.sendEmail(usermail, recmail, subject, message, pass);
                            Log.i("Mail", "Sent successfully");
                        } catch (Exception e) {
                            //System.out.println("Exception");
                            Log.i("Mail", "Exception");
                            e.printStackTrace();
                        }
                    }
                }


            }
            else if(cursor3!=null && cursor3.getCount()>0)
            {
                if(cursor3.moveToFirst())
                {
                    String dept_id = cursor3.getString(cursor3.getColumnIndex("Dept_ID"));
                    recmail = databaseAccess.getDeptMail(dept_id);
                    recname = "all IT students";
                    subject = "A new survey is opened for you";

                    message = "Dear " + recname + ", \n A new survey Survey No-" + survid + " has been" +
                            " opened for you ";

                    try {
                        //System.out.println("Mail Sent Successfully");

                        SendEmail.sendEmail(usermail, recmail, subject, message, pass);
                        Log.i("Mail", "Sent successfully");
                    } catch (Exception e) {
                        //System.out.println("Exception");
                        Log.i("Mail", "Exception");
                        e.printStackTrace();
                    }
                }
            }
            else if(inOrg)
            {
                recmail = "";
                recname = "all students";
                subject = "A new survey is opened for you";

                message = "Dear " + recname + ", \n A new survey Survey No-" + survid + " has been" +
                        " opened for you ";

                try {
                    //System.out.println("Mail Sent Successfully");

                    SendEmail.sendEmail(usermail, recmail, subject, message, pass);
                    Log.i("Mail", "Sent successfully");
                } catch (Exception e) {
                    //System.out.println("Exception");
                    Log.i("Mail", "Exception");
                    e.printStackTrace();
                }

            }
        }
        else{
            String pass = databaseAccess.getPassword(userid);
            String rec_id = databaseAccess.getUserFromSurv(survid);
            String recname = databaseAccess.getName(rec_id);
            String recmail = databaseAccess.getEmail(rec_id);
            String usermail = databaseAccess.getEmail(userid);
            String subject = "Regarding Survey Apporval";
            String message = "Dear "+recname+", \n Your Survey with ID-"+survid+" has been cancelled.";
            try {
                StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().
                        permitAll().build();
                StrictMode.setThreadPolicy(threadPolicy);
                SendEmail.sendEmail(usermail, recmail, subject, message, pass);
               System.out.println("Mail Sent Successfully");
            }
            catch (Exception e)
            {
                System.out.println("Exception");
                Log.i("Mail", "Exception");
                e.printStackTrace();
            }
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
