package com.example.quicksurvey;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    private DatabaseAccess(Context context)
    {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open()
    {
        this.db = openHelper.getWritableDatabase();
    }

    public void close(){
        if(db!=null)
        {
            this.db.close();
        }
    }

    public String getPassword(String name)
    {
        c = db.rawQuery("select * from User where User_ID='" + name+"'",
                null);


        if(c != null)
        {
            if  (c.moveToFirst()) {
                do {
                    String data = c.getString(c.getColumnIndex("Password"));
                    System.out.println(data);
                    return data;
                }while (c.moveToNext());
            }
        }

        return null;
    }

    public void insertSurvey(int id, String name, String deadline)
    {
        String access = "yes";
        String sql = "insert into Survey values ('"+id+"', '"+name+"', '"+access+"', '"+deadline+"')";
        db.execSQL(sql);
    }

    public void insertQues(int id, String que, int survid)
    {
        db.execSQL("insert into Questions values ('"+id+"', '"+que+"')");

        db.execSQL("insert into SurvQue values ('"+survid+"', '"+id+"')");
    }

    public void insertOpt(int options_id, String opt1, String opt2, String opt3, String opt4,
                          int ques_id)
    {
        db.execSQL("insert into Options values('"+options_id+"', '"+opt1+"', '"+opt2+"', '"+opt3+"', '"+opt4+"')");

        db.execSQL("insert into QueOpt values('"+ques_id+"', '"+options_id+"')");
    }

    public int getMaxQue()
    {
        c = null;
        c = db.rawQuery("select count(*) from Questions", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToNext())
            {
                int temp = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
                return temp+1;
            }
        }
        else{
            return 1;
        }
        return 1;
    }

    public int getMaxSurv()
    {

        c = null;
        c = db.rawQuery("select count(*) from Survey", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                int temp = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
                return temp+1;
            }
        }
        else{
            return 1;
        }
        return 1;
    }

    public int getMaxOpt()
    {
        c = null;
        c = db.rawQuery("select count(*) from Options", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToNext())
            {
                int temp = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
                return temp+1;
            }
        }
        else{
            return 1;
        }
        return 1;
    }

    public void insertSurvinUser(int surv_id, String user_id)
    {
        db.execSQL("insert into SurvUser values ('"+surv_id+"', '"+user_id+"')");
        String approv = "class_bunker";
        String status = "pending";
        db.execSQL("insert into SurvApp values ('"+surv_id+"', '"+approv+"' , '"+status+"')");
    }

    public Cursor getNotfications()
    {
        c = null;
        String status = "pending";
        c = db.rawQuery("select Survey_ID from SurvApp where Approval='"+status+"'",
                null);

        return c;
    }

    public void getApproval(int survid)
    {
        String status = "approve";
        db.execSQL("update SurvApp set Approval='"+status+"' where Survey_ID='"+survid+"'");

    }

    public void getCancel(int survid)
    {
        String status = "cancel";
        db.execSQL("update SurvApp set Approval='"+status+"' where Survey_ID='"+survid+"'");
    }

    public Cursor getSurvforUser(String user_id)
    {
        c = null;
        String status = "approve";
        c = db.rawQuery("select SurvUser.Survey_ID from SurvUser" +
                " inner join SurvApp where SurvUser.Survey_ID=SurvApp.Survey_ID " +
                "and SurvUser.User_ID='"+user_id+"' and SurvApp.Approval='"+status+"' ", null);

        return c;
    }

    public Cursor getQueFromSurv(int survid)
    {
        c = null;
        c = db.rawQuery("select Question_ID from SurvQue where Survey_ID='"+survid+"'", null);
        return c;
    }

    public String getQue(int que_id)
    {
        c = null;
        c = db.rawQuery("select Name from Questions where Question_ID='"+que_id+"'", null);

        if(c!=null && c.getCount()>0)
        {
            while(c.moveToNext())
            {
                String que = c.getString(c.getColumnIndex("Name"));
                return que;
            }
        }
        else{
            return "";
        }

        return "";
    }

    public int getoptid(int que_id)
    {
        c = null;
        c = db.rawQuery("SELECT Option_ID FROM QueOpt WHERE Question_ID ='"+que_id+"'", null);


        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {

                int opt_id = Integer.parseInt(c.getString(c.getColumnIndex("Option_ID")));
                return opt_id;
            }
            else{
                return 0;
            }
        }

        return 0;
    }

    public ArrayList<String> getOptions(int opt_id)
    {
        c = null;
        c = db.rawQuery("select Option1, Option2, Option3, Option4 from Options where Option_ID='"+opt_id+"'",null);
        ArrayList<String>options;
        options = new ArrayList<>();
        if(c.moveToFirst())
        {
            options.add(0, c.getString(c.getColumnIndex("Option1")));
            options.add(1, c.getString(c.getColumnIndex("Option2")));
            options.add(2, c.getString(c.getColumnIndex("Option3")));
            options.add(3, c.getString(c.getColumnIndex("Option4")));

            return options;
        }

        return null;
    }

    public int getMaxResp()
    {
        c = null;
        c = db.rawQuery("select count(*) from Responses", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToNext())
            {
                int temp = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
                return temp+1;
            }
        }
        else{
            return 1;
        }
        return 1;
    }

    public void insertResp(int resp_id, int choice, String userid, int surveyid, int queid)
    {
        db.execSQL("insert into Responses values ('"+resp_id+"', '"+choice+"')");
        db.execSQL("insert into UserResp values ('"+userid+"', '"+surveyid+"', '"+queid+"', '"+resp_id+"')");

    }

    public String findUser(String user_id)
    {
        c = null;
        c = db.rawQuery("select Name from User where User_ID='"+user_id+"'", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                String name = c.getString(c.getColumnIndex("Name"));
                return name;
            }
        }

        return null;
    }

    public String findEmail(String user_id)
    {
        c = null;
        c = db.rawQuery("select Email_ID from User where User_ID='"+user_id+"'", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                String name = c.getString(c.getColumnIndex("Email_ID"));
                return name;
            }
        }

        return null;
    }

    public void setemail(String email, String userid)
    {
        db.execSQL("update User set Email_ID='"+email+"' where User_ID='"+userid+"'");
    }

    public void setPassword(String passid, String userid)
    {
        db.execSQL("update User set Password='"+passid+"' where User_ID='"+userid+"'");
    }

    public void insertSurvinOrg(int surv_id)
    {
        db.execSQL("insert into SurvOrg values('"+surv_id+"')");
        String approv = "class_bunker";
        String status = "pending";
        db.execSQL("insert into SurvApp values ('"+surv_id+"', '"+approv+"' , '"+status+"')");
    }

    public void insertSurvinDept(int surv_id, String dept_id)
    {
        db.execSQL("insert into SurvDept values('"+surv_id+"', '"+dept_id+"')");
        String approv = "class_bunker";
        String status = "pending";
        db.execSQL("insert into SurvApp values ('"+surv_id+"', '"+approv+"' , '"+status+"')");
    }

    public void insertSurvinGrp(int surv_id, String grp_id)
    {
        db.execSQL("insert into SurvGrp values('"+surv_id+"', '"+grp_id+"')");
        String approv = "class_bunker";
        String status = "pending";
        db.execSQL("insert into SurvApp values ('"+surv_id+"', '"+approv+"' , '"+status+"')");
    }




}
