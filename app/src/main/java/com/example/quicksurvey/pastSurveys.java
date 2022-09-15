package com.example.quicksurvey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class pastSurveys extends AppCompatActivity {

    ListView listView;
    ArrayList<Survey> surveys;

    String user_id;
    String usertype;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_surveys);

        final Intent intent = getIntent();
        user_id = intent.getStringExtra("userid");
        usertype = intent.getStringExtra("usertype");
        Toast.makeText(this, user_id, Toast.LENGTH_SHORT).show();

        listView = (ListView)findViewById(R.id.atsurveylist);
        //listView.setExpand(true);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(pastSurveys.this);
        databaseAccess.open();
        Cursor cursor = databaseAccess.getSurvfromResp(user_id);

        surveys = new ArrayList<>();

        if(cursor!=null && cursor.getCount()>0)
        {
            while(cursor.moveToNext())
            {
                int surv_id = cursor.getInt(cursor.getColumnIndex("Survey_ID"));
                String name = databaseAccess.getNameFromSurv(surv_id);

                surveys.add(new Survey(name,surv_id,""));
            }

            if(surveys.size()==0)
            {
                surveys.add(new Survey("No surveys attempted", -1, ""));
            }

            adapter = new SurveyListAdapter(getApplicationContext(), R.layout.survey_template, surveys);
            listView.setAdapter(adapter);
        }
        else{
            Toast.makeText(this, "No Past surveys", Toast.LENGTH_SHORT).show();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int surv_id = Integer.parseInt(((TextView)view.findViewById(R.id.surveyid2)).
                        getText().toString());
                Intent intent1 = new Intent(pastSurveys.this, pastresponse.class);
                intent1.putExtra("userid", user_id);
                intent1.putExtra("surveyid", surv_id);
                intent1.putExtra("usertype", usertype);
                startActivity(intent1);
            }
        });

    }
}
