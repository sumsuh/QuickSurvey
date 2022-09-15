package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        Button submit = (Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String temp = username.getText().toString();
                System.out.println(temp);
                if(temp.equals("class_bunker"))
                {
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
                    databaseAccess.open();
                    String temp2 = databaseAccess.getPassword(temp);

                    if(password.getText().toString().equals(temp2))
                    {
                        Intent intent = new Intent(getApplicationContext(), Admin.class);
                        intent.putExtra("userid", username.getText().toString());
                        intent.putExtra("usertype", "admin");
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Invalid Username or Password",
                                Toast.LENGTH_SHORT).show();
                    }


                }
                else{

                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
                    databaseAccess.open();
                    String temp2 = databaseAccess.getPassword(temp);
                    databaseAccess.close();

                    if(password.getText().toString().equals(temp2))
                    {
                        Intent intent = new Intent(getApplicationContext(), User.class);
                        intent.putExtra("userid", username.getText().toString());
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Invalid Username or Password",
                                Toast.LENGTH_SHORT).show();
                    }



                }

            }
        });
    }



}
