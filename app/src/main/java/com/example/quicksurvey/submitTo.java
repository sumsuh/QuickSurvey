package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class submitTo extends AppCompatActivity {

    int survid;
    EditText survname;
    EditText deadline;
    EditText depname;
    EditText grpno;
    EditText user_id;
    RadioButton org;
    RadioButton dept;
    RadioButton grp;
    RadioButton indiv;
    RadioGroup rdgrp1;
    RadioGroup rdgrp2;
    String userid;
    String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_to);

        Intent intent = getIntent();
        survid =  Integer.parseInt(intent.getStringExtra("surveyid"));
        userid = intent.getStringExtra("userid");
        usertype = intent.getStringExtra("usertype");

        survname = (EditText)findViewById(R.id.surveyname);
        deadline = (EditText)findViewById(R.id.deadline);
        depname = (EditText)findViewById(R.id.depname);
        grpno = (EditText)findViewById(R.id.grpno);
        user_id = (EditText)findViewById(R.id.user_id);
        org = (RadioButton)findViewById(R.id.org);
        dept = (RadioButton)findViewById(R.id.dep);
        grp = (RadioButton)findViewById(R.id.grp);
        indiv = (RadioButton)findViewById(R.id.indiv);
        rdgrp1 = (RadioGroup)findViewById(R.id.rdgrp1);
        rdgrp2 = (RadioGroup)findViewById(R.id.rdgrp2);

        rdgrp1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable


                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    grp.setChecked(false);
                    indiv.setChecked(false);

                    if(checkedId == R.id.org)
                    {
                        depname.setFocusable(false);
                        depname.setFocusableInTouchMode(false);
                        depname.setClickable(false);

                        grpno.setFocusable(false);
                        grpno.setFocusableInTouchMode(false);
                        grpno.setClickable(false);

                        user_id.setFocusable(false);
                        user_id.setFocusableInTouchMode(false);
                        user_id.setClickable(false);
                    }
                    else if(checkedId == R.id.dep)
                    {
                        depname.setFocusable(true);
                        depname.setFocusableInTouchMode(true);
                        depname.setClickable(true);

                        grpno.setFocusable(false);
                        grpno.setFocusableInTouchMode(false);
                        grpno.setClickable(false);

                        user_id.setFocusable(false);
                        user_id.setFocusableInTouchMode(false);
                        user_id.setClickable(false);
                    }
                }
            }
        });

        rdgrp2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    dept.setChecked(false);
                    org.setChecked(false);

                    if(checkedId == R.id.grp)
                    {
                        depname.setFocusable(false);
                        depname.setFocusableInTouchMode(false);
                        depname.setClickable(false);

                        grpno.setFocusable(true);
                        grpno.setFocusableInTouchMode(true);
                        grpno.setClickable(true);

                        user_id.setFocusable(false);
                        user_id.setFocusableInTouchMode(false);
                        user_id.setClickable(false);
                    }
                    else if(checkedId == R.id.indiv)
                    {
                        depname.setFocusable(false);
                        depname.setFocusableInTouchMode(false);
                        depname.setClickable(false);

                        grpno.setFocusable(false);
                        grpno.setFocusableInTouchMode(false);
                        grpno.setClickable(false);

                        user_id.setFocusable(true);
                        user_id.setFocusableInTouchMode(true);
                        user_id.setClickable(true);
                    }
                }
            }
        });



    }



    public void goToHome(View view)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(submitTo.this);
        databaseAccess.open();
        databaseAccess.insertSurvey(survid,
                survname.getText().toString(), deadline.getText().toString(), userid);
        if(org.isChecked()){
            databaseAccess.insertSurvinOrg(survid);
        }
        else if(dept.isChecked())
        {
            databaseAccess.insertSurvinDept(survid, depname.getText().toString());
        }
        else if(grp.isChecked())
        {
            databaseAccess.insertSurvinGrp(survid, grpno.getText().toString());
        }
        else if(indiv.isChecked()){
            databaseAccess.insertSurvinUser(survid, user_id.getText().toString());
        }

        String pass = databaseAccess.getPassword(userid);
        String username = databaseAccess.getName(userid);
        String message = username + " is requesting you to approve the survey with id" +
                Integer.toString(survid);
        String subject = "Request for Approval";

        String appmail = databaseAccess.getApproverMail();
        String usermail = databaseAccess.getEmail(userid);


        try {
            StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().
                    permitAll().build();
            StrictMode.setThreadPolicy(threadPolicy);
            SendEmail.sendEmail(usermail, appmail, subject, message, pass);
            Log.i("Mail", "Sent successfully");
        }
        catch (Exception e)
        {
            Log.i("Mail", "Exception");
            e.printStackTrace();
        }

        if(usertype.equals("user")) {
            Intent intent = new Intent(submitTo.this, User.class);

            intent.putExtra("userid", userid);
            intent.putExtra("usertype", usertype);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(submitTo.this, Admin.class);

            intent.putExtra("userid", userid);
            intent.putExtra("usertype", usertype);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


    }
}
