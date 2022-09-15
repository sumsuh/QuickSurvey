package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class pastresponse extends AppCompatActivity {

    TextView pastque;
    TextView opt1;
    TextView opt2;
    TextView opt3;
    TextView opt4;
    TextView per1;
    TextView per2;
    TextView per3;
    TextView per4;
    View view1;
    View view2;
    View view3;
    View view4;
    int surv_id;
    String user_id;
    String usertype;
    Cursor cursor;
    DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pastresponse);

        Intent intent = getIntent();
        surv_id = intent.getIntExtra("surveyid", 0);
        user_id = intent.getStringExtra("userid");
        usertype = intent.getStringExtra("usertype");


        pastque = (TextView)findViewById(R.id.pastque);
        opt1 = (TextView)findViewById(R.id.pastopt1);
        opt2 = (TextView)findViewById(R.id.pastque2);
        opt3 = (TextView)findViewById(R.id.pastque3);
        opt4 = (TextView)findViewById(R.id.pastque4);

        per1 = (TextView)findViewById(R.id.per1);
        per2 = (TextView)findViewById(R.id.per2);
        per3 = (TextView)findViewById(R.id.per3);
        per4 = (TextView)findViewById(R.id.per4);

        view1 = (View)findViewById(R.id.view1);
        view2 = (View)findViewById(R.id.view2);
        view3 = (View)findViewById(R.id.view3);
        view4 = (View)findViewById(R.id.view4);

        databaseAccess = DatabaseAccess.getInstance(pastresponse.this);
        databaseAccess.open();
        cursor = databaseAccess.getQueFromSurv(surv_id);
        if(cursor != null && cursor.getCount()>0)
        {
            if(cursor.moveToFirst()){
                int que_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                        "Question_ID")));
                String que = databaseAccess.getQue(que_id);
                pastque.setText(que);

                ArrayList<String> options;

                int opt_id = databaseAccess.getoptid(que_id);

                options = databaseAccess.getOptions(opt_id);

                opt1.setText(options.get(0));
                opt2.setText(options.get(1));
                opt3.setText(options.get(2));
                opt4.setText(options.get(3));

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
                per1.setText(Double.toString(optper1));
                per2.setText(Double.toString(optper2));
                per3.setText(Double.toString(optper3));
                per4.setText(Double.toString(optper4));

                System.out.println(optper1);
                System.out.println(optper2);
                System.out.println(optper3);
                System.out.println(optper4);
                view1.setLayoutParams(new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.MATCH_PARENT, optper1));
                view2.setLayoutParams(new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.MATCH_PARENT, optper2));
                view3.setLayoutParams(new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.MATCH_PARENT, optper3));
                view4.setLayoutParams(new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.MATCH_PARENT, optper4));
                int resp_id = databaseAccess.getRespFromUser(user_id,surv_id,que_id);

                String getOfferedAns = databaseAccess.offeredAns(resp_id);
                System.out.println(getOfferedAns);
                if(getOfferedAns.equals("1"))
                {
                    view1.setBackgroundResource(R.color.selected);
                    view2.setBackgroundResource(R.color.notselected);
                    view3.setBackgroundResource(R.color.notselected);
                    view4.setBackgroundResource(R.color.notselected);

                }
                else if(getOfferedAns.equals("2"))
                {
                    view1.setBackgroundResource(R.color.notselected);
                    view2.setBackgroundResource(R.color.selected);
                    view3.setBackgroundResource(R.color.notselected);
                    view4.setBackgroundResource(R.color.notselected);
                }
                else if(getOfferedAns.equals("3"))
                {
                    view1.setBackgroundResource(R.color.notselected);
                    view2.setBackgroundResource(R.color.notselected);
                    view3.setBackgroundResource(R.color.selected);
                    view4.setBackgroundResource(R.color.notselected);
                }
                else if(getOfferedAns.equals("4"))
                {
                    view1.setBackgroundResource(R.color.notselected);
                    view2.setBackgroundResource(R.color.notselected);
                    view3.setBackgroundResource(R.color.notselected);
                    view4.setBackgroundResource(R.color.selected);
                }

            }
        }


    }

    public void nextResp(View view)
    {
        if(cursor.isAfterLast())
        {
            cursor.close();
            //databaseAccess.close();
            if(usertype.equals("user"))
            {
                Intent intent = new Intent(pastresponse.this, User.class);
                intent.putExtra("userid", user_id);
                intent.putExtra("usertype", usertype);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
            else{
                Intent intent = new Intent(pastresponse.this, Admin.class);
                intent.putExtra("userid", user_id);
                intent.putExtra("usertype", usertype);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        }
        else{
            if(cursor.moveToNext())
            {
                int que_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                        "Question_ID")));
                String que = databaseAccess.getQue(que_id);
                pastque.setText(que);

                ArrayList<String> options;

                int opt_id = databaseAccess.getoptid(que_id);

                options = databaseAccess.getOptions(opt_id);

                opt1.setText(options.get(0));
                opt2.setText(options.get(1));
                opt3.setText(options.get(2));
                opt4.setText(options.get(3));

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
                per1.setText(Double.toString(optper1));
                per2.setText(Double.toString(optper2));
                per3.setText(Double.toString(optper3));
                per4.setText(Double.toString(optper4));

                view1.setLayoutParams(new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.MATCH_PARENT, optper1));
                view2.setLayoutParams(new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.MATCH_PARENT, optper2));
                view3.setLayoutParams(new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.MATCH_PARENT, optper3));
                view4.setLayoutParams(new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.MATCH_PARENT, optper4));
                int resp_id = databaseAccess.getRespFromUser(user_id,surv_id,que_id);

                String getOfferedAns = databaseAccess.offeredAns(resp_id);
                System.out.println(getOfferedAns);
                if(getOfferedAns.equals("1"))
                {
                    view1.setBackgroundResource(R.color.selected);
                    view2.setBackgroundResource(R.color.notselected);
                    view3.setBackgroundResource(R.color.notselected);
                    view4.setBackgroundResource(R.color.notselected);

                }

                if(getOfferedAns.equals("2"))
                {
                    view1.setBackgroundResource(R.color.notselected);
                    view2.setBackgroundResource(R.color.selected);
                    view3.setBackgroundResource(R.color.notselected);
                    view4.setBackgroundResource(R.color.notselected);
                }

                if(getOfferedAns.equals("3"))
                {
                    view1.setBackgroundResource(R.color.notselected);
                    view2.setBackgroundResource(R.color.notselected);
                    view3.setBackgroundResource(R.color.selected);
                    view4.setBackgroundResource(R.color.notselected);
                }

                if(getOfferedAns.equals("4"))
                {
                    view1.setBackgroundResource(R.color.notselected);
                    view2.setBackgroundResource(R.color.notselected);
                    view3.setBackgroundResource(R.color.notselected);
                    view4.setBackgroundResource(R.color.selected);
                }

            }
            else{
                cursor.close();
                //databaseAccess.close();
                if(usertype.equals("user"))
                {
                    Intent intent = new Intent(pastresponse.this, User.class);

                    intent.putExtra("userid", user_id);
                    intent.putExtra("usertype", usertype);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                else{
                    Intent intent = new Intent(pastresponse.this, Admin.class);

                    intent.putExtra("userid", user_id);
                    intent.putExtra("usertype", usertype);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        }
    }
}
