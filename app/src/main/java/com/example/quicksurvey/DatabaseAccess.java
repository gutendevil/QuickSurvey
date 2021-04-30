package com.example.quicksurvey;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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



}
