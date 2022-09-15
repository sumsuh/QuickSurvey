package com.example.quicksurvey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Admin extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    ListView surveyAvailable;
    ListView surveygrp;
    ListView surveydept;
    ListView surveyorg;
    ArrayList<Survey> surveys;
    ArrayList<Survey> surveys2;
    ArrayList<Survey> surveys3;
    ArrayList<Survey> surveys4;

    private Timer timer1;
    private Timer timer2;
    private Timer timer3;
    private Timer timer4;
    private DatabaseAccess databaseAccess;

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

    public class CustomTimerTask extends TimerTask {

        private Handler mHandler = new Handler();
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {


                            for(Survey s:surveys)
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
                                databaseAccess.open();
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

    public class CustomTimerTask2 extends TimerTask {

        private Handler mHandler = new Handler();
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {


                            for(Survey s:surveys2)
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
                                databaseAccess.open();
                                String deadline = databaseAccess.getDeadline(surv_id);
                                try {
                                    Date date2 = sdf.parse(deadline);
                                    deadline = printDifference(date,date2);
                                    s.setDeadline(deadline);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                adapter2.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }).start();
        }
    }

    public class CustomTimerTask3 extends TimerTask {

        private Handler mHandler = new Handler();
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {


                            for(Survey s:surveys3)
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
                                adapter3.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }).start();
        }
    }

    public class CustomTimerTask4 extends TimerTask {

        private Handler mHandler = new Handler();
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {


                            for(Survey s:surveys4)
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
                                databaseAccess.open();
                                String deadline = databaseAccess.getDeadline(surv_id);
                                try {
                                    Date date2 = sdf.parse(deadline);
                                    deadline = printDifference(date,date2);
                                    s.setDeadline(deadline);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                adapter4.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }).start();
        }
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
    private SurveyListAdapter adapter3;
    private SurveyListAdapter adapter4;
    private String user_id;


    private SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("userid");
        surveyAvailable = (ListView)findViewById(R.id.surveyAvailable);
        surveygrp = (ListView)findViewById(R.id.surveygrp);
        surveydept = (ListView)findViewById(R.id.surveydept);
        surveyorg = (ListView)findViewById(R.id.surveyorg);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        TextView displayname = (TextView)header.findViewById(R.id.displayname);
        TextView displayemail = (TextView)header.findViewById(R.id.displayemail);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.allsurveys:
                        Intent intent1 = new Intent(Admin.this, allsurveys.class);
                        intent1.putExtra("usertype", "admin");
                        intent1.putExtra("userid", user_id);
                        startActivity(intent1);
                        return true;
                    case R.id.mysurveys:
                        Intent intent2 = new Intent(Admin.this, mysurveys.class);
                        intent2.putExtra("usertype", "admin");
                        intent2.putExtra("userid", user_id);
                        startActivity(intent2);
                        return true;
                    case R.id.atsurveys:
                        Intent intent = new Intent(Admin.this, pastSurveys.class);
                        intent.putExtra("usertype", "admin");
                        intent.putExtra("userid", user_id);
                        startActivity(intent);
                        return true;
                    case R.id.calendar:
                        Intent intent3 = new Intent(Admin.this, calendar.class);
                        intent3.putExtra("usertype", "admin");
                        intent3.putExtra("userid", user_id);
                        startActivity(intent3);
                        return true;
                    case R.id.settings:
                        Intent intent5 = new Intent(Admin.this, settings.class);
                        intent5.putExtra("usertype", "admin");
                        intent5.putExtra("userid", user_id);
                        startActivity(intent5);
                        return true;
                    case R.id.notifications:
                        Intent intent4 = new Intent(Admin.this, notifications.class);
                        intent4.putExtra("usertype", "admin");
                        intent4.putExtra("userid", user_id);
                        startActivity(intent4);
                        return true;
                    case R.id.logout:
                        finish();
                        return true;
                    default:
                        return false;
                }

            }
        });
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        databaseAccess = DatabaseAccess.getInstance(Admin.this);
        databaseAccess.open();

        String disname = databaseAccess.getName(user_id);
        String dismail = databaseAccess.findEmail(user_id);
        System.out.println(disname+" "+dismail);
        displayname.setText(disname);
        displayemail.setText(dismail);

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        Date date = new Date();
        try {
            date = sdf.parse(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Cursor cursor = databaseAccess.getSurvforUser(user_id, timeStamp);
        surveys = new ArrayList<>();

        if(cursor!=null && cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                int surv_id = cursor.getInt(cursor.getColumnIndex("Survey_ID"));
                String name = databaseAccess.getNameFromSurv(surv_id);
                String deadline = databaseAccess.getDeadline(surv_id);


                if(databaseAccess.getRespCount(surv_id,user_id)==0)
                {
                    try {
                        Date date2 = sdf.parse(deadline);
                        deadline = printDifference(date,date2);
                        Survey survey = new Survey(name, surv_id, deadline);
                        surveys.add(survey);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }

            if(surveys.size()==0)
            {
                surveys.add(new Survey("No survey available", 0, ""));
            }
            else{
                timer1 = new Timer();
                TimerTask updateProfile = new CustomTimerTask();
                timer1.scheduleAtFixedRate(updateProfile, 0, 1000);

                surveyAvailable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int surv_id = Integer.parseInt(((TextView)view.findViewById(R.id.surveyid2)).
                                getText().toString());
                        Intent intent = new Intent(Admin.this, attemptSurvey.class);
                        intent.putExtra("usertype", "admin");
                        intent.putExtra("userid", user_id);
                        intent.putExtra("surveyid", surv_id);
                        startActivity(intent);

                    }
                });
            }
            adapter = new SurveyListAdapter(getApplicationContext(),
                    R.layout.survey_template, surveys);

            surveyAvailable.setAdapter(adapter);
        }
        else{
            if(surveys.size()==0)
            {
                surveys.add(new Survey("No survey available", 0, ""));
            }
            else{
                timer1 = new Timer();
                TimerTask updateProfile = new CustomTimerTask();
                timer1.scheduleAtFixedRate(updateProfile, 0, 1000);

                surveyAvailable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int surv_id = Integer.parseInt(((TextView)view.findViewById(R.id.surveyid2)).
                                getText().toString());
                        Intent intent = new Intent(Admin.this, attemptSurvey.class);
                        intent.putExtra("usertype", "admin");
                        intent.putExtra("userid", user_id);
                        intent.putExtra("surveyid", surv_id);
                        startActivity(intent);

                    }
                });
            }
            adapter = new SurveyListAdapter(getApplicationContext(),
                    R.layout.survey_template, surveys);

            surveyAvailable.setAdapter(adapter);
        }

        surveys2 = new ArrayList<>();
        Cursor cursor1 = databaseAccess.getGrpfromUser(user_id);
        if(cursor1!=null && cursor1.getCount()>0)
        {
            Cursor cursor2;
            while (cursor1.moveToNext())
            {
                String grp = cursor1.getString(cursor1.getColumnIndex("Group_ID"));

                System.out.println(grp);
                cursor2 = databaseAccess.getSurvfromGrp(grp);
                if(cursor2!=null && cursor2.getCount()>0)
                {
                    if(cursor2.moveToFirst()) {
                        int surv_id = cursor2.getInt(cursor2.getColumnIndex("Survey_ID"));
                        String name = databaseAccess.getNameFromSurv(surv_id);
                        String deadline = databaseAccess.getDeadline(surv_id);

                        if (databaseAccess.getRespCount(surv_id, user_id) == 0) {
                            try {
                                Date date2 = sdf.parse(deadline);
                                deadline = printDifference(date, date2);
                                Survey survey = new Survey(name, surv_id, deadline);
                                surveys2.add(survey);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }

            if(surveys2.size()==0)
            {
                surveys2.add(new Survey("No survey available", 0, ""));
            }
            else{
                timer2 = new Timer();
                TimerTask updateProfile = new CustomTimerTask2();
                timer2.scheduleAtFixedRate(updateProfile, 0, 1000);

                surveygrp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int surv_id = Integer.parseInt(((TextView)view.findViewById(R.id.surveyid2)).
                                getText().toString());
                        Intent intent = new Intent(Admin.this, attemptSurvey.class);
                        intent.putExtra("usertype", "admin");
                        intent.putExtra("userid", user_id);
                        intent.putExtra("surveyid", surv_id);
                        startActivity(intent);
                    }
                });
            }
            adapter2 = new SurveyListAdapter(getApplicationContext(),
                    R.layout.survey_template, surveys2);

            surveygrp.setAdapter(adapter2);
        }
        else{
            if(surveys2.size()==0)
            {
                surveys2.add(new Survey("No survey available", 0, ""));
            }
            else{
                timer2 = new Timer();
                TimerTask updateProfile = new CustomTimerTask2();
                timer2.scheduleAtFixedRate(updateProfile, 0, 1000);

                surveygrp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int surv_id = Integer.parseInt(((TextView)view.findViewById(R.id.surveyid2)).
                                getText().toString());
                        Intent intent = new Intent(Admin.this, attemptSurvey.class);
                        intent.putExtra("usertype", "admin");
                        intent.putExtra("userid", user_id);
                        intent.putExtra("surveyid", surv_id);
                        startActivity(intent);
                    }
                });
            }
            adapter2 = new SurveyListAdapter(getApplicationContext(),
                    R.layout.survey_template, surveys2);

            surveygrp.setAdapter(adapter2);
        }

        surveys3 = new ArrayList<>();

        String dept = databaseAccess.getDeptfromUser(user_id);
        Cursor cursor2 = databaseAccess.getSurvfromDept(dept);
        if(cursor2!=null && cursor2.getCount()>0)
        {
            while (cursor2.moveToNext())
            {
                int surv_id = Integer.parseInt(cursor2.getString(
                        cursor2.getColumnIndex("Survey_ID")));
                String name = databaseAccess.getNameFromSurv(surv_id);
                String deadline = databaseAccess.getDeadline(surv_id);

                if(databaseAccess.getRespCount(surv_id,user_id)==0)
                {
                    try {
                        Date date2 = sdf.parse(deadline);
                        deadline = printDifference(date,date2);
                        Survey survey = new Survey(name, surv_id, deadline);
                        surveys3.add(survey);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(surveys3.size()==0)
            {
                surveys3.add(new Survey("No survey available", 0, ""));
            }
            else{
                timer3 = new Timer();
                TimerTask updateProfile = new CustomTimerTask3();
                timer3.scheduleAtFixedRate(updateProfile, 0, 1000);

                surveydept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int surv_id = Integer.parseInt(((TextView)view.findViewById(R.id.surveyid2)).
                                getText().toString());
                        Intent intent = new Intent(Admin.this, attemptSurvey.class);
                        intent.putExtra("usertype", "admin");
                        intent.putExtra("userid", user_id);
                        intent.putExtra("surveyid", surv_id);
                        startActivity(intent);
                    }
                });
            }

            adapter3 = new SurveyListAdapter(getApplicationContext(),
                    R.layout.survey_template, surveys3);
            surveydept.setAdapter(adapter3);
        }
        else{
            if(surveys3.size()==0)
            {
                surveys3.add(new Survey("No survey available", 0, ""));
            }
            else{
                timer3 = new Timer();
                TimerTask updateProfile = new CustomTimerTask3();
                timer3.scheduleAtFixedRate(updateProfile, 0, 1000);

                surveydept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int surv_id = Integer.parseInt(((TextView)view.findViewById(R.id.surveyid2)).
                                getText().toString());
                        Intent intent = new Intent(Admin.this, attemptSurvey.class);
                        intent.putExtra("usertype", "admin");
                        intent.putExtra("userid", user_id);
                        intent.putExtra("surveyid", surv_id);
                        startActivity(intent);
                    }
                });
            }

            adapter3 = new SurveyListAdapter(getApplicationContext(),
                    R.layout.survey_template, surveys3);
            surveydept.setAdapter(adapter3);
        }

        surveys4 = new ArrayList<>();
        Cursor cursor3 = databaseAccess.getSurvfromOrg();
        if(cursor3!=null && cursor3.getCount()>0)
        {
            while (cursor3.moveToNext())
            {

                int surv_id = Integer.parseInt(cursor3.getString(
                        cursor3.getColumnIndex("Survey_ID")));
                String name = databaseAccess.getNameFromSurv(surv_id);
                String deadline = databaseAccess.getDeadline(surv_id);

                if(databaseAccess.getRespCount(surv_id,user_id)==0)
                {
                    try {
                        Date date2 = sdf.parse(deadline);
                        deadline = printDifference(date,date2);
                        Survey survey = new Survey(name, surv_id, deadline);
                        surveys4.add(survey);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(surveys4.size()==0)
            {
                surveys4.add(new Survey("No survey available", 0, ""));
            }
            else{
                timer4 = new Timer();
                TimerTask updateProfile = new CustomTimerTask4();
                timer4.scheduleAtFixedRate(updateProfile, 0, 1000);

                surveyorg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int surv_id = Integer.parseInt(((TextView)view.findViewById(R.id.surveyid2)).
                                getText().toString());
                        Intent intent = new Intent(Admin.this, attemptSurvey.class);
                        intent.putExtra("usertype", "admin");
                        intent.putExtra("userid", user_id);
                        intent.putExtra("surveyid", surv_id);
                        startActivity(intent);
                    }
                });
            }

            adapter4 = new SurveyListAdapter(getApplicationContext(),
                    R.layout.survey_template, surveys4);

            surveyorg.setAdapter(adapter4);
        }
        else {
            if(surveys4.size()==0)
            {
                surveys4.add(new Survey("No survey available", 0, ""));
            }
            else{
                timer4 = new Timer();
                TimerTask updateProfile = new CustomTimerTask4();
                timer4.scheduleAtFixedRate(updateProfile, 0, 1000);

                surveyorg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int surv_id = Integer.parseInt(((TextView)view.findViewById(R.id.surveyid2)).
                                getText().toString());
                        Intent intent = new Intent(Admin.this, attemptSurvey.class);
                        intent.putExtra("usertype", "admin");
                        intent.putExtra("userid", user_id);
                        intent.putExtra("surveyid", surv_id);
                        startActivity(intent);
                    }
                });
            }

            adapter4 = new SurveyListAdapter(getApplicationContext(),
                    R.layout.survey_template, surveys4);

            surveyorg.setAdapter(adapter4);
        }


    }

    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    public void addSurvey(View view)
    {
        Intent intent = new Intent(Admin.this, createSurvey.class);
        intent.putExtra("userid", user_id);
        intent.putExtra("usertype", "admin");


        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(Admin.this);
        databaseAccess.open();
        int survid = databaseAccess.getMaxSurv();

        intent.putExtra("surveyid", Integer.toString(survid));
        startActivity(intent);
    }
}
