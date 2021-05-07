package com.example.quicksurvey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class allsurveys extends AppCompatActivity {

    private ListView alllivesurveys;
    private ListView allpastsurveys;
    private ArrayList<String> list;
    private ArrayList<String> list2;
    private ArrayAdapter adapter;
    private ArrayAdapter adapter2;

    String userid;
    String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allsurveys);

        Intent intent = getIntent();

        userid = intent.getStringExtra("userid");
        usertype = intent.getStringExtra("usertype");


        alllivesurveys = (ListView)findViewById(R.id.alllivesurveys);
        allpastsurveys = (ListView)findViewById(R.id.allPastSurveys);
        registerForContextMenu(alllivesurveys);
        registerForContextMenu(allpastsurveys);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(allsurveys.this);
        databaseAccess.open();

        String timeStamp = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        Cursor cursor = databaseAccess.getAllLiveSurveys(timeStamp);

        list = new ArrayList<>();
        if(cursor!=null && cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                String temp = cursor.getString(cursor.getColumnIndex("Survey_ID"));
                list.add(temp);
            }

        }

        if(list.size()==0)
        {
            list.add("No live surveys");
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        alllivesurveys.setAdapter(adapter);

        Cursor cursor1 = databaseAccess.getAllPastSurveys(timeStamp);

        list2 = new ArrayList<>();

        if(cursor1!=null && cursor1.getCount()>0)
        {
            while(cursor1.moveToNext())
            {
                String temp = cursor1.getString(cursor1.getColumnIndex("Survey_ID"));
                list2.add(temp);
            }

        }

        adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list2);

        allpastsurveys.setAdapter(adapter2);

        alllivesurveys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int surv_id = Integer.parseInt(alllivesurveys.getItemAtPosition(position).toString());
                Intent intent1 = new Intent(allsurveys.this, seeresults.class);
                intent1.putExtra("surveyid", surv_id);
                intent1.putExtra("userid", userid);
                intent1.putExtra("usertype", usertype);
                startActivity(intent1);
            }
        });

        allpastsurveys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int surv_id = Integer.parseInt(allpastsurveys.getItemAtPosition(position).toString());
                Intent intent1 = new Intent(allsurveys.this, seeresults.class);
                intent1.putExtra("surveyid", surv_id);
                intent1.putExtra("userid", userid);
                intent1.putExtra("usertype", usertype);
                startActivity(intent1);
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v.getId() == R.id.alllivesurveys)
        {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.long_press, menu);
        }
        else if(v.getId() == R.id.allPastSurveys)
        {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.long_press_past, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();

        switch (item.getItemId())
        {
            case R.id.exportdata:
                int pos = info.position;
                String temp = alllivesurveys.getItemAtPosition(pos).toString();
                int surv_id = Integer.parseInt(temp);
                exportDB(surv_id);
                return true;
            case R.id.cancelsurvey:
                pos = info.position;
                temp = alllivesurveys.getItemAtPosition(pos).toString();
                surv_id = Integer.parseInt(temp);
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(allsurveys.this);
                databaseAccess.open();
                databaseAccess.toCancel(surv_id);
                databaseAccess.close();
                return true;
            case R.id.exportdata2:
                pos = info.position;
                temp = allpastsurveys.getItemAtPosition(pos).toString();
                surv_id = Integer.parseInt(temp);
                exportDB(surv_id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }


    }

    private void exportDB(int surv_id) {

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(allsurveys.this);
        databaseAccess.open();

        databaseAccess.dropIfExists("Data");
        databaseAccess.createTable();

        Cursor cursor = databaseAccess.getQueFromSurv(surv_id);
        if(cursor != null && cursor.getCount()>0)
        {
            while (cursor.moveToNext()){
                int que_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                        "Question_ID")));
                String que = databaseAccess.getQue(que_id);


                ArrayList<String> options;

                int opt_id = databaseAccess.getoptid(que_id);

                options = databaseAccess.getOptions(opt_id);



                int[] count = new int[]{0, 0, 0, 0};
                for(int i = 0; i < 4; i++)
                {
                    count[i] = databaseAccess.getOptcount(i+1, que_id);
                }

                float optper1 = ((float)count[0]/(float) (count[0]+count[1]+count[2]+count[3]))*100;
                float optper2 = ((float)count[1]/(float)(count[0]+count[1]+count[2]+count[3]))*100;
                float optper3 = ((float)count[2]/(float)(count[0]+count[1]+count[2]+count[3]))*100;
                float optper4 = ((float)count[3]/(float)(count[0]+count[1]+count[2]+count[3]))*100;
                optper1 = Math.round(optper1*100.0)/100;
                optper2 = Math.round(optper2*100.0)/100;
                optper3 = Math.round(optper3*100.0)/100;
                optper4 = Math.round(optper4*100.0)/100;

                String temp1 = String.valueOf(optper1) + "%";
                String temp2 = String.valueOf(optper2) + "%";
                String temp3 = String.valueOf(optper3) + "%";
                String temp4 = String.valueOf(optper4) + "%";

                databaseAccess.insertIntoData(que, temp1, temp2, temp3, temp4);
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                    READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this,
                        new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, 1);
            }
        }

        File exportDir = new File("/sdcard/QuickSurvey");
        Toast.makeText(this, exportDir.getPath(), Toast.LENGTH_SHORT).show();
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "Data.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

            Cursor curCSV = databaseAccess.readData();
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1),
                        curCSV.getString(2), curCSV.getString(3),
                        curCSV.getString(4)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            databaseAccess.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("AllSurveys", sqlEx.getMessage(), sqlEx);
        }
    }

}
