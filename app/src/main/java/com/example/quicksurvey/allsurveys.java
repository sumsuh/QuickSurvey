package com.example.quicksurvey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class allsurveys extends AppCompatActivity {

    private ListView alllivesurveys;
    private ListView allpastsurveys;
    private ArrayList<Survey> list;
    private ArrayList<Survey> list2;


    private DatabaseAccess databaseAccess;

    String userid;
    String usertype;
    private Timer timer;

    SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public static String printDifference(Date startDate, Date endDate) throws ParseException{

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();



        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        //long elapsedDays = different / daysInMilli;
        //different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String temp = Long.toString(elapsedHours)+":"+Long.toString(elapsedMinutes)+":"+Long.toString(elapsedSeconds);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");

        Date date = sdf2.parse(temp);
        return sdf2.format(date);


    }

    private class SurveyListAdapter extends ArrayAdapter<Survey> {

        private Context mContext;
        int mresource;


        public SurveyListAdapter(@NonNull Context context, int resource, ArrayList<Survey> objects) {
            super(context, resource, objects);
            mContext = context;
            mresource = resource;


        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String name = getItem(position).getName();
            int surveyid = getItem(position).getSurveyid();
            String deadline = getItem(position).getDeadline();

            Survey survey = new Survey(name, surveyid, deadline);

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mresource, parent, false);

            TextView txtname = convertView.findViewById(R.id.surveyname);
            TextView txtid = convertView.findViewById(R.id.surveyid2);
            TextView txtdeadine = convertView.findViewById(R.id.surveydeadline);

            txtname.setText(name);
            txtid.setText(Integer.toString(surveyid));
            txtdeadine.setText(deadline);


            convertView.setBackgroundResource(R.drawable.rounded_edges2);
            return convertView;
        }
    }
    private SurveyListAdapter adapter;
    private SurveyListAdapter adapter2;

    public class CustomTimerTask extends TimerTask{

        private Handler mHandler = new Handler();
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {


                            for(Survey s:list)
                            {
                                String timeStamp = new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                                Date date = new Date();
                                try {
                                    date = sdf.parse(timeStamp);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                int surv_id = s.getSurveyid();
                                String deadline = databaseAccess.getDeadline(surv_id);
                                try {
                                    Date date2 = sdf.parse(deadline);
                                    deadline = printDifference(date,date2);
                                    s.setDeadline(deadline);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }).start();
        }
    }
    void setTimerForAdvertise() {
        timer = new Timer();
        TimerTask updateProfile = new CustomTimerTask();
        timer.scheduleAtFixedRate(updateProfile, 0, 1000);
    }

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

        databaseAccess = DatabaseAccess.getInstance(allsurveys.this);
        databaseAccess.open();

        String timeStamp = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        Cursor cursor = databaseAccess.getAllLiveSurveys(timeStamp);

        Date date = new Date();
        try {
            date = sdf.parse(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        list = new ArrayList<>();
        if(cursor!=null && cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                int surv_id = cursor.getInt(cursor.getColumnIndex("Survey_ID"));
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                String deadline = cursor.getString(cursor.getColumnIndex("Deadline"));
                try {
                    Date date2 = sdf.parse(deadline);
                    deadline = printDifference(date,date2);
                    Survey survey = new Survey(name, surv_id, deadline);
                    list.add(survey);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }

        if(list.size()==0)
        {
            list.add(new Survey("No live surveys", 0, ""));
        }
        else{
            setTimerForAdvertise();
        }

        adapter = new SurveyListAdapter(this, R.layout.survey_template, list);

        alllivesurveys.setAdapter(adapter);

        Cursor cursor1 = databaseAccess.getAllPastSurveys(timeStamp);

        list2 = new ArrayList<>();

        if(cursor1!=null && cursor1.getCount()>0)
        {
            while(cursor1.moveToNext())
            {
                int surv_id = cursor1.getInt(cursor1.getColumnIndex("Survey_ID"));
                String name = cursor1.getString(cursor1.getColumnIndex("Name"));
                Survey survey = new Survey(name, surv_id, "");
                list2.add(survey);
            }

        }

        if(list2.size()==0)
        {
            list2.add(new Survey("No surveys created", 0, ""));
        }

        adapter2 = new SurveyListAdapter(this, R.layout.survey_template, list2);

        allpastsurveys.setAdapter(adapter2);

        alllivesurveys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int surv_id = Integer.parseInt(((TextView)view.findViewById(R.id.surveyid2)).
                        getText().toString());
                //Toast.makeText(allsurveys.this, Integer.toString(surv_id), Toast.LENGTH_SHORT).show();
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
                int surv_id = Integer.parseInt(((TextView)view.findViewById(R.id.surveyid2)).
                        getText().toString());
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
                Survey survey = (Survey)adapter.getItem(pos);

                int surv_id = survey.getSurveyid();
                Toast.makeText(this, Integer.toString(surv_id), Toast.LENGTH_SHORT).show();
                exportDB(surv_id);
                return true;
            case R.id.cancelsurvey:
                pos = info.position;
                survey = (Survey)adapter.getItem(pos);
                surv_id = survey.getSurveyid();
                Toast.makeText(this, Integer.toString(surv_id), Toast.LENGTH_SHORT).show();
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(allsurveys.this);
                databaseAccess.open();
                if(usertype.equals("admin"))
                {
                    databaseAccess.getCancel(surv_id);
                }
                else if(databaseAccess.approval(surv_id).equals("pending"))
                {
                    databaseAccess.getCancel(surv_id);
                }
                else {
                    databaseAccess.toCancel(surv_id);
                }

                return true;
            case R.id.exportdata2:
                pos = info.position;
                survey = (Survey)adapter2.getItem(pos);
                surv_id = survey.getSurveyid();
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

        }
        catch(Exception sqlEx)
        {
            Log.e("AllSurveys", sqlEx.getMessage(), sqlEx);
        }
    }

}
