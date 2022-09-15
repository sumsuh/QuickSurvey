package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static java.sql.DriverManager.println;

public class attemptSurvey extends AppCompatActivity {

    int surv_id;
    String userid;
    String usertype;
    Cursor cursor;
    TextView attemptque;
    RadioButton optionA;
    RadioButton optionB;
    RadioButton optionC;
    RadioButton optionD;
    DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempt_survey);

        Intent intent = getIntent();
        surv_id = intent.getIntExtra("surveyid",0);
        userid = intent.getStringExtra("userid");
        usertype = intent.getStringExtra("usertype");

        attemptque = (TextView) findViewById(R.id.attemptque);
        optionA = (RadioButton) findViewById(R.id.optiona);
        optionB = (RadioButton) findViewById(R.id.optionb);
        optionC = (RadioButton) findViewById(R.id.optionc);
        optionD = (RadioButton) findViewById(R.id.optiond);

        databaseAccess = DatabaseAccess.getInstance(attemptSurvey.this);
        databaseAccess.open();

        cursor = databaseAccess.getQueFromSurv(surv_id);

        if(cursor != null && cursor.getCount()>0)
        {
            if(cursor.moveToFirst()){
                int que_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                        "Question_ID")));
                String que = databaseAccess.getQue(que_id);
                attemptque.setText(que);

                ArrayList<String> options;

                int opt_id = databaseAccess.getoptid(que_id);

                options = databaseAccess.getOptions(opt_id);

                optionA.setText(options.get(0));
                optionB.setText(options.get(1));
                optionC.setText(options.get(2));
                optionD.setText(options.get(3));

            }


        }

    }

    public void getNextQue(View view)
    {
        if(cursor.isAfterLast())
        {

            cursor.close();
            //databaseAccess.close();

            if(usertype.equals("user"))
            {
                Intent intent = new Intent(attemptSurvey.this, User.class);
                intent.putExtra("userid",userid);
                intent.putExtra("usertype",usertype);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
            else{
                Intent intent = new Intent(attemptSurvey.this, Admin.class);
                intent.putExtra("userid",userid);
                intent.putExtra("usertype",usertype);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        }
        else{
            int que_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                    "Question_ID")));

            int resp_id = databaseAccess.getMaxResp();
            if(optionA.isChecked())
            {
                databaseAccess.insertResp(resp_id, 1, userid, surv_id, que_id);

            }
            else if(optionB.isChecked())
            {
                System.out.println("hello");
                databaseAccess.insertResp(resp_id, 2, userid, surv_id, que_id);
            }
            else if(optionC.isChecked())
            {
                databaseAccess.insertResp(resp_id, 3, userid, surv_id, que_id);
            }
            else if(optionD.isChecked()){
                databaseAccess.insertResp(resp_id, 4, userid, surv_id, que_id);
            }



            if(cursor.moveToNext())
            {
                que_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                        "Question_ID")));
                String que = databaseAccess.getQue(que_id);
                attemptque.setText(que);

                ArrayList<String> options;

                int opt_id = databaseAccess.getoptid(que_id);

                options = databaseAccess.getOptions(opt_id);

                optionA.setText(options.get(0));
                optionB.setText(options.get(1));
                optionC.setText(options.get(2));
                optionD.setText(options.get(3));
            }
            else{
                cursor.close();
                //databaseAccess.close();
                if(usertype.equals("user"))
                {
                    Intent intent = new Intent(attemptSurvey.this, User.class);
                    intent.putExtra("userid",userid);
                    intent.putExtra("usertype",usertype);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(attemptSurvey.this, Admin.class);
                    intent.putExtra("userid",userid);
                    intent.putExtra("usertype",usertype);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }

            }
        }


    }
}
