package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class createSurvey extends AppCompatActivity {

    EditText question;
    EditText option1;
    EditText option2;
    EditText option3;
    EditText option4;
    int survid;
    String user_id;
    String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_survey);

        question = (EditText)findViewById(R.id.question);
        option1 = (EditText)findViewById(R.id.option1);
        option2 = (EditText)findViewById(R.id.option2);
        option3 = (EditText)findViewById(R.id.option3);
        option4 = (EditText)findViewById(R.id.option4);

        Intent intent = getIntent();
        survid = Integer.parseInt(intent.getStringExtra("surveyid"));
        user_id = intent.getStringExtra("userid");
        usertype = intent.getStringExtra("usertype");


    }

    public void submit(View view)
    {
        Intent intent = new Intent(createSurvey.this, submitTo.class);
        intent.putExtra("usertype", usertype);
        intent.putExtra("userid", user_id);
        intent.putExtra("surveyid", Integer.toString(survid));
        startActivity(intent);
    }

    public void addQuestion(View view)
    {
        String que = question.getText().toString();
        String opt1 = option1.getText().toString();
        String opt2 = option2.getText().toString();
        String opt3 = option3.getText().toString();
        String opt4 = option4.getText().toString();

        question.getText().clear();
        option1.getText().clear();
        option2.getText().clear();
        option3.getText().clear();
        option4.getText().clear();

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(createSurvey.this);
        databaseAccess.open();
        int id = databaseAccess.getMaxQue();
        databaseAccess.insertQues(id, que, survid);
        int options_id = databaseAccess.getMaxOpt();
        databaseAccess.insertOpt(options_id, opt1, opt2, opt3, opt4, id);

    }
}
