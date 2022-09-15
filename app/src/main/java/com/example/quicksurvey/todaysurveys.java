package com.example.quicksurvey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class todaysurveys extends AppCompatActivity {

    private ListView surveys;
    ArrayList<Survey> list;


    String userid;
    String usertype;
    String deadline;
    private DatabaseAccess databaseAccess;
    private Timer timer;

    private SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");



    public static String printDifference(Date startDate, Date endDate) throws ParseException{

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

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
        setContentView(R.layout.activity_todaysurveys);

        surveys = (ListView)findViewById(R.id.datesurveys);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        usertype = intent.getStringExtra("usertype");
        deadline = intent.getStringExtra("deadline");

        try {
            Date date = sdf.parse(deadline);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            Date dt = c.getTime();
            String deadline2 = sdf.format(dt);

            databaseAccess = DatabaseAccess.getInstance(todaysurveys.this);
            databaseAccess.open();
            Cursor cursor = databaseAccess.getDateSurveys(deadline,deadline2);

            String timeStamp = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            Date date3 = new Date();
            try {
                date3 = sdf.parse(timeStamp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            list = new ArrayList<>();
            if(cursor!=null && cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    int surv_id = cursor.getInt(
                            cursor.getColumnIndex("Survey_ID"));
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

                if(list.size() == 0)
                {
                    list.add(new Survey("No surveys ending this date", -1, ""));

                }
                else{
                    setTimerForAdvertise();
                }

                adapter = new SurveyListAdapter(getApplicationContext(),
                        R.layout.survey_template, list);

                surveys.setAdapter(adapter);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
