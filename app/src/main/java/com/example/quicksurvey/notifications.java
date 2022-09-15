package com.example.quicksurvey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class notifications extends AppCompatActivity {

    Cursor cursor;
    ListView listView;
    ArrayList<Survey>list;

    String userid;
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
        setContentView(R.layout.activity_notifications);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        usertype = intent.getStringExtra("usertype");

        listView = (ListView)findViewById(R.id.notify_list);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(notifications.this);
        databaseAccess.open();
        cursor = databaseAccess.getNotfications();

        list = new ArrayList<>();
        if(cursor!=null && cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                int surv_id = cursor.getInt(cursor.getColumnIndex("Survey_ID"));
                String name = databaseAccess.getNameFromSurv(surv_id);
                list.add(new Survey(name, surv_id, ""));

            }

            adapter = new SurveyListAdapter(getApplicationContext(),
                    R.layout.survey_template, list);
            listView.setAdapter(adapter);
        }
        else{
            Toast.makeText(notifications.this, "No data to show", Toast.LENGTH_SHORT).show();
        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int surv_id = Integer.parseInt(((TextView)view.findViewById(R.id.surveyid2)).
                        getText().toString());
                Intent intent = new Intent(notifications.this, notifications2.class);
                intent.putExtra("surveyid", surv_id);
                DatabaseAccess databaseAccess1 = DatabaseAccess.getInstance(notifications.this);
                databaseAccess1.open();
                intent.putExtra("userid", userid);
                intent.putExtra("usertype", usertype);

                String app = databaseAccess1.approval(surv_id);
                intent.putExtra("approval", app);
                startActivity(intent);
            }
        });

    }

}
