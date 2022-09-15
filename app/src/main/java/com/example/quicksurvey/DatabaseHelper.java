package com.example.quicksurvey;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "quicksurvey.db";
    public static final String DBLOCATION = "/data/data/com.example.quicksurvey/databases/";
    private Context mContext;
    public SQLiteDatabase mDatabase;
    Cursor c = null;

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;

        boolean dbexist = checkdatabase();
        if (dbexist) {
            openDatabase();
        } else {
            System.out.println("Database doesn't exist");
            try {
                createdatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void createdatabase() throws IOException {
        boolean dbexist = checkdatabase();
        if(!dbexist) {
            this.getReadableDatabase();
            copyDatabase();
        }
    }

    private boolean checkdatabase() {

        boolean checkdb = false;
        try {
            String myPath = DBLOCATION + DBNAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch(SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return checkdb;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if(mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if(mDatabase!=null) {
            mDatabase.close();
        }
    }

    public void copyDatabase() {

        InputStream myInput;
        OutputStream outStream;
        try {
            myInput = mContext.getAssets().open(DBNAME);
            String file = DBLOCATION + DBNAME;
            outStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = myInput.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            outStream.flush();
            myInput.close();
            outStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public String getAddress(String name)
    {

        c = mDatabase.rawQuery("select Email_ID from User where User_ID='" + name+"'",
                null);


        if(c != null)
        {
            if  (c.moveToFirst()) {
                do {
                    String data = c.getString(c.getColumnIndex("Password"));
                    System.out.println(data);
                    return data;
                }while (c.moveToNext());
            }
        }
        c.close();

        return null;

    }
}


