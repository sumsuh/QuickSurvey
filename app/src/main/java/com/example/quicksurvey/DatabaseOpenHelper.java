package com.example.quicksurvey;

import android.content.Context;


import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;



public class DatabaseOpenHelper extends SQLiteAssetHelper {

    private static String mDbName = "quicksurvey.db";
    private static int mDbVersion = 2;


    public DatabaseOpenHelper(Context context) {
        super(context, mDbName, null, mDbVersion);

    }



}