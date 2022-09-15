package com.example.quicksurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class settings extends AppCompatActivity {

    String userid;
    String usertype;
    TextView username;
    EditText email;
    EditText pass1;
    EditText pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        username = (TextView)findViewById(R.id.username);
        email = (EditText)findViewById(R.id.email);
        pass1 = (EditText)findViewById(R.id.pass1);
        pass2 = (EditText)findViewById(R.id.pass2);

        Intent intent = getIntent();

        userid = intent.getStringExtra("userid");
        usertype = intent.getStringExtra("usertype");
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(settings.this);
        databaseAccess.open();

        username.setText(databaseAccess.findUser(userid));
        email.setText(databaseAccess.findEmail(userid));



    }

    public void goToHome(View view)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(settings.this);
        databaseAccess.open();
        if(view.getId() == R.id.button7) {
            databaseAccess.setemail(email.getText().toString(), userid);
            if (pass1.getText().toString().length() != 0 && pass1.getText().toString() != null) {
                if (pass2.getText().toString().length() != 0 && pass2.getText().toString() != null) {
                    String temppass = databaseAccess.getPassword(userid);
                    if (pass1.getText().toString().equals(temppass)) {
                        databaseAccess.setPassword(pass2.getText().toString(), userid);
                        Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        if (usertype.equals("user")) {
                            Intent intent = new Intent(settings.this, User.class);
                            intent.putExtra("userid", userid);
                            intent.putExtra("usertype", usertype);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } else {
                            Intent intent = new Intent(settings.this, Admin.class);
                            intent.putExtra("userid", userid);
                            intent.putExtra("usertype", usertype);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    } else {
                        Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Enter New Password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                if (usertype.equals("user")) {
                    Intent intent = new Intent(settings.this, User.class);
                    intent.putExtra("userid", userid);
                    intent.putExtra("usertype", usertype);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(settings.this, Admin.class);
                    intent.putExtra("userid", userid);
                    intent.putExtra("usertype", usertype);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }

            }
        }
        else{
            if (usertype.equals("user")) {
                Intent intent = new Intent(settings.this, User.class);
                intent.putExtra("userid", userid);
                intent.putExtra("usertype", usertype);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else {
                Intent intent = new Intent(settings.this, Admin.class);
                intent.putExtra("userid", userid);
                intent.putExtra("usertype", usertype);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }

    }
}
