package com.example.quicksurvey;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    private DatabaseAccess(Context context)
    {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open()
    {
        this.db = openHelper.getWritableDatabase();
    }

    public void close(){
        if(db!=null)
        {
            this.db.close();
        }
    }

    public String getPassword(String name)
    {
        c = db.rawQuery("select * from User where User_ID='" + name+"'",
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

        return null;
    }

    public void insertSurvey(int id, String name, String deadline, String user_id)
    {
        String access = "yes";
        String sql = "insert into Survey values ('"+id+"', '"+name+"', '"+access+"'," +
                " '"+deadline+"', '"+user_id+"')";
        db.execSQL(sql);
    }

    public void insertQues(int id, String que, int survid)
    {
        db.execSQL("insert into Questions values ('"+id+"', '"+que+"')");

        db.execSQL("insert into SurvQue values ('"+survid+"', '"+id+"')");
    }

    public void insertOpt(int options_id, String opt1, String opt2, String opt3, String opt4,
                          int ques_id)
    {
        db.execSQL("insert into Options values('"+options_id+"', '"+opt1+"', '"+opt2+"', '"+opt3+"', '"+opt4+"')");

        db.execSQL("insert into QueOpt values('"+ques_id+"', '"+options_id+"')");
    }

    public int getMaxQue()
    {
        c = null;
        c = db.rawQuery("select count(*) from Questions", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToNext())
            {
                int temp = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
                return temp+1;
            }
        }
        else{
            return 1;
        }
        return 1;
    }

    public int getMaxSurv()
    {

        c = null;
        c = db.rawQuery("select count(*) from Survey", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                int temp = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
                return temp+1;
            }
        }
        else{
            return 1;
        }
        return 1;
    }

    public int getMaxOpt()
    {
        c = null;
        c = db.rawQuery("select count(*) from Options", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToNext())
            {
                int temp = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
                return temp+1;
            }
        }
        else{
            return 1;
        }
        return 1;
    }

    public void insertSurvinUser(int surv_id, String user_id)
    {
        db.execSQL("insert into SurvUser values ('"+surv_id+"', '"+user_id+"')");
        String approv = "class_bunker";
        String status = "pending";
        db.execSQL("insert into SurvApp values ('"+surv_id+"', '"+approv+"' , '"+status+"')");
    }

    public Cursor getNotfications()
    {
        c = null;
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                format(Calendar.getInstance().getTime());

        String status = "pending";
        String status2 = "tocancel";
        c = db.rawQuery("select Survey_ID from SurvApp where (Approval='"+status+"' or" +
                        " Approval='"+status2+"') and Survey_ID in (select Survey_ID from Survey" +
                        " where Deadline>'"+timeStamp+"')",
                null);

        return c;
    }

    public void getApproval(int survid)
    {
        String status = "approve";
        db.execSQL("update SurvApp set Approval='"+status+"' where Survey_ID='"+survid+"'");

    }

    public void getCancel(int survid)
    {
        String status = "cancel";
        db.execSQL("update SurvApp set Approval='"+status+"' where Survey_ID='"+survid+"'");

        db.execSQL("DELETE FROM Options WHERE Option_ID IN" +
                "(SELECT Option_ID FROM QueOpt WHERE Question_ID IN" +
                "(SELECT Question_ID FROM SurvQue WHERE Survey_ID='"+survid+"'));");

        db.execSQL("delete from QueOpt where Question_ID in " +
                "(SELECT Question_ID FROM SurvQue WHERE Survey_ID='"+survid+"')");

        db.execSQL("DELETE FROM Questions WHERE Question_ID IN " +
                "(SELECT Question_ID FROM SurvQue WHERE Survey_ID='"+survid+"');");

        db.execSQL("DELETE FROM SurvQue WHERE Survey_ID IN" +
                "(SELECT Survey_ID FROM SurvApp WHERE Survey_ID='"+survid+"');");

        db.execSQL("DELETE FROM SurvOrg where Survey_ID in (SELECT\n" +
                "Survey_ID from SurvApp WHERE Survey_ID='"+survid+"');");

        db.execSQL("DELETE FROM SurvUser where Survey_ID in (SELECT\n" +
                "Survey_ID from SurvApp where Survey_ID='"+survid+"');");

        db.execSQL("DELETE FROM SurvDept where Survey_ID in (SELECT\n" +
                "Survey_ID from SurvApp where Survey_ID='"+survid+"');");

        db.execSQL("DELETE FROM SurvGrp where Survey_ID in (SELECT\n" +
                "Survey_ID from SurvApp where Survey_ID='"+survid+"');");

        db.execSQL("DELETE FROM Survey WHERE Survey_ID in (select Survey_ID" +
                " from Survey where Survey_ID='"+survid+"');");
        db.execSQL("DELETE FROM SurvApp WHERE Survey_ID='"+survid+"'");

    }

    public Cursor getSurvforUser(String user_id, String timestamp)
    {
        c = null;
        String status = "approve";
        String access = "yes";
        c = db.rawQuery("select SurvUser.Survey_ID from SurvUser" +
                " inner join SurvApp where SurvUser.Survey_ID=SurvApp.Survey_ID " +
                "and SurvUser.Survey_ID in (" +
                "select Survey_ID from Survey WHERE " +
                "Accessibility = '"+access+"' and Deadline>'"+timestamp+"')" +
                "and SurvUser.User_ID='"+user_id+"' " +
                "and SurvApp.Approval='"+status+"' ", null);

        return c;
    }

    public Cursor getQueFromSurv(int survid)
    {
        c = null;
        c = db.rawQuery("select Question_ID from SurvQue where Survey_ID='"+survid+"'", null);
        return c;
    }

    public String getQue(int que_id)
    {
        c = null;
        c = db.rawQuery("select Name from Questions where Question_ID='"+que_id+"'", null);

        if(c!=null && c.getCount()>0)
        {
            while(c.moveToNext())
            {
                String que = c.getString(c.getColumnIndex("Name"));
                return que;
            }
        }
        else{
            return "";
        }

        return "";
    }

    public int getoptid(int que_id)
    {
        c = null;
        c = db.rawQuery("SELECT Option_ID FROM QueOpt WHERE Question_ID ='"+que_id+"'", null);


        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {

                int opt_id = Integer.parseInt(c.getString(c.getColumnIndex("Option_ID")));
                return opt_id;
            }
            else{
                return 0;
            }
        }

        return 0;
    }

    public ArrayList<String> getOptions(int opt_id)
    {
        c = null;
        c = db.rawQuery("select Option1, Option2, Option3, Option4 from Options where Option_ID='"+opt_id+"'",null);
        ArrayList<String>options;
        options = new ArrayList<>();
        if(c!=null && c.getCount()>0) {
            if (c.moveToFirst()) {
                options.add(0, c.getString(c.getColumnIndex("Option1")));
                options.add(1, c.getString(c.getColumnIndex("Option2")));
                options.add(2, c.getString(c.getColumnIndex("Option3")));
                options.add(3, c.getString(c.getColumnIndex("Option4")));

                return options;
            }
        }

        return null;
    }

    public int getMaxResp()
    {
        c = null;
        c = db.rawQuery("select count(*) from Responses", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToNext())
            {
                int temp = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
                return temp+1;
            }
        }
        else{
            return 1;
        }
        return 1;
    }

    public void insertResp(int resp_id, int choice, String userid, int surveyid, int queid)
    {
        db.execSQL("insert into Responses values ('"+resp_id+"', '"+choice+"')");
        db.execSQL("insert into UserResp values ('"+userid+"', '"+surveyid+"', '"+queid+"', '"+resp_id+"')");

    }

    public String findUser(String user_id)
    {
        c = null;
        c = db.rawQuery("select Name from User where User_ID='"+user_id+"'", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                String name = c.getString(c.getColumnIndex("Name"));
                return name;
            }
        }

        return null;
    }

    public String findEmail(String user_id)
    {
        c = null;
        c = db.rawQuery("select Email_ID from User where User_ID='"+user_id+"'", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                String name = c.getString(c.getColumnIndex("Email_ID"));
                return name;
            }
        }

        return null;
    }

    public void setemail(String email, String userid)
    {
        db.execSQL("update User set Email_ID='"+email+"' where User_ID='"+userid+"'");
    }

    public void setPassword(String passid, String userid)
    {
        db.execSQL("update User set Password='"+passid+"' where User_ID='"+userid+"'");
    }

    public void insertSurvinOrg(int surv_id)
    {
        db.execSQL("insert into SurvOrg values('"+surv_id+"')");
        String approv = "class_bunker";
        String status = "pending";
        db.execSQL("insert into SurvApp values ('"+surv_id+"', '"+approv+"' , '"+status+"')");
    }

    public void insertSurvinDept(int surv_id, String dept_id)
    {
        db.execSQL("insert into SurvDept values('"+surv_id+"', '"+dept_id+"')");
        String approv = "class_bunker";
        String status = "pending";
        db.execSQL("insert into SurvApp values ('"+surv_id+"', '"+approv+"' , '"+status+"')");
    }

    public void insertSurvinGrp(int surv_id, String grp_id)
    {
        db.execSQL("insert into SurvGrp values('"+surv_id+"', '"+grp_id+"')");
        String approv = "class_bunker";
        String status = "pending";
        db.execSQL("insert into SurvApp values ('"+surv_id+"', '"+approv+"' , '"+status+"')");
    }

    public Cursor getSurvfromResp(String user_id)
    {
        c = null;
        c = db.rawQuery("select distinct(Survey_ID) from UserResp where User_ID='"+user_id+"'", null);
        return c;
    }

    public Cursor getSurvfromGrp(String grp_id)
    {
        c = null;
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                format(Calendar.getInstance().getTime());

        String status = "approve";
        c = db.rawQuery("select SurvGrp.Survey_ID as Survey_ID from SurvGrp, SurvApp where SurvGrp.Group_ID='"+grp_id+"' " +
                "and SurvGrp.Survey_ID=SurvApp.Survey_ID and SurvApp.Approval='"+status+"' " +
                "and SurvGrp.Survey_ID in (select Survey_ID from Survey where Deadline>'"+timestamp+"')", null);
        return c;
    }

    public Cursor getSurvfromDept(String dept_id)
    {
        c = null;
        String status = "approve";
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                format(Calendar.getInstance().getTime());
        c = db.rawQuery("select SurvDept.Survey_ID as Survey_ID from SurvDept, SurvApp " +
                "where SurvDept.Dept_ID='"+dept_id+"' " +
                "and SurvDept.Survey_ID=SurvApp.Survey_ID and SurvApp.Approval='"+status+"' "+
                "and SurvDept.Survey_ID in (select Survey_ID from Survey " +
                "where Deadline>'"+timestamp+"')", null);
        return c;
    }

    public Cursor getSurvfromOrg()
    {
        c = null;
        String status = "approve";
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                format(Calendar.getInstance().getTime());
        c = db.rawQuery("select SurvOrg.Survey_ID as Survey_ID from SurvOrg, SurvApp " +
                "where SurvOrg.Survey_ID=SurvApp.Survey_ID and SurvApp.Approval='"+status+"' "+
                "and SurvOrg.Survey_ID in (select Survey_ID from Survey "  +
                "where Deadline>'"+timestamp+"')", null);
        return c;
    }

    public Cursor getGrpfromUser(String user_id)
    {
        c = null;
        c = db.rawQuery("select Group_ID from UserGrp where User_ID='"+user_id+"'", null);
        return c;
    }

    public String getDeptfromUser(String user_id)
    {
        c = null;
        c = db.rawQuery("select Dept_ID from UserDept where User_ID='"+user_id+"'", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                String dept_id = c.getString(c.getColumnIndex("Dept_ID"));
                return dept_id;
            }
        }
        return null;
    }

    public int getRespFromUser(String user_id, int surv_id, int que_id)
    {
        c = null;
        c = db.rawQuery("select Response_ID from UserResp where User_ID='"+user_id+"' and" +
                " Survey_ID='"+surv_id+"' and Question_ID='"+que_id+"'", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                int resp_id = c.getInt(c.getColumnIndex("Response_ID"));
                return resp_id;
            }
        }

        return -1;
    }

    public String offeredAns(int resp_id)
    {
        c = null;
        c = db.rawQuery("select OfferedAns from Responses where Response_ID='"+resp_id+"'", null);
        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                String offans = c.getString(c.getColumnIndex("OfferedAns"));
                return offans;
            }
        }

        return "";
    }



    public int getOptcount(int offered_ans, int que_id)
    {
        c = null;
        c = db.rawQuery("select count(*) from Responses where OfferedAns='"+offered_ans+"' " +
                "and Response_ID in (SELECT Response_ID from UserResp where Question_ID='"+que_id+"')",
                null);

        if(c!= null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                int cn = c.getInt(c.getColumnIndex("count(*)"));
                return cn;
            }
        }

        return 0;
    }

    public int getRespCount(int surv_id, String userid)
    {
        c = null;
        c = db.rawQuery("select count(Response_ID) from UserResp where Survey_ID='"+surv_id+"'" +
                " and User_ID='"+userid+"'", null);
        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                int count = c.getInt(c.getColumnIndex("count(Response_ID)"));
                return count;
            }
        }
        return 0;
    }

    public Cursor getLiveSurveys(String user_id, String deadline)
    {
        c = null;
        c = db.rawQuery("select Survey_ID, Name, Deadline from Survey where User_ID='"+user_id+"' and" +
                " Deadline>'"+deadline+"'", null);

        return c;
    }

    public Cursor getPastSurveys(String user_id, String deadline)
    {
        c = null;
        c = db.rawQuery("select Survey_ID, Name from Survey where User_ID='"+user_id+"' and" +
                " Deadline<='"+deadline+"'", null);

        return c;
    }

    public Cursor getLiveSurveys2( String deadline)
    {
        c = null;
        c = db.rawQuery("select Survey_ID,Deadline,Name from Survey" +
                " where " +
                " Deadline>'"+deadline+"'", null);

        return c;
    }

    public Cursor getDateSurveys(String deadline, String deadline2)
    {
        c = null;
        String status = "approve";
        String status2 = "tocancel";
        c = db.rawQuery("select Survey_ID, Name, Deadline from Survey where Deadline>='"+deadline+"' " +
                "and Deadline<'"+deadline2+"' and Survey_ID in (select Survey_ID" +
                " from SurvApp where (Approval='"+status+"' or Approval='"+status2+"'))", null);

        return c;
    }



    public void createTable()
    {
        db.execSQL("create table Data (Name varchar(200), Option1 char(10), Option2 char(10)," +
                " Option3 char(10), Option4 char(10))");
    }

    public void insertIntoData(String name, String opt1, String opt2, String opt3, String opt4)
    {
        db.execSQL("insert into Data values('"+name+"', '"+opt1+"', '"+opt2+"', '"+opt3+"', " +
                "'"+opt4+"')");
    }

    public Cursor readData()
    {
        c = null;
        c = db.rawQuery("select * from Data", null);
        return c;
    }

    public void dropIfExists(String tableName) {


        String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            if(cursor!=null) {
                if(cursor.getCount()>0) {
                   cursor.close();
                   db.execSQL("drop table Data");
                }
            }

        }


    }


    public String approval(int surv_id)
    {
        c = null;
        c = db.rawQuery("select Approval from SurvApp where Survey_ID='"+surv_id+"'", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                String temp = c.getString(0);
                return temp;
            }

        }

        return "";
    }

    public void toCancel(int surv_id)
    {
        String status = "tocancel";
        db.execSQL("update SurvApp set Approval='"+status+"' where Survey_ID='"+surv_id+"'");
    }

    public Cursor getAllLiveSurveys(String deadline)
    {
        c = null;
        c = db.rawQuery("select Survey_ID, Name, Deadline from Survey where Deadline>'"+deadline+"'", null);
        return c;
    }

    public Cursor getAllPastSurveys(String deadline)
    {
        c = null;
        c = db.rawQuery("select Survey_ID, Name from Survey where Deadline<='"+deadline+"'", null);
        return c;
    }

    public String getName(String userid)
    {
        c = null;
        c = db.rawQuery("select Name from User where User_ID='"+userid+"'", null);
        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                String temp = c.getString(c.getColumnIndex("Name"));
                return temp;
            }
        }

        return "";
    }

    public String getApproverMail()
    {
        c = null;
        c = db.rawQuery("select * from Approver", null);
        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                String email = c.getString(c.getColumnIndex("Email_ID"));
                return email;
            }
        }

        return "";
    }

    public String getEmail(String user_id)
    {
        c = null;
        c = db.rawQuery("select Email_ID from User where User_ID='"+user_id+"'", null);
        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                String email = c.getString(c.getColumnIndex("Email_ID"));
                return email;
            }
        }
        return "";
    }

    public String getUserFromSurv(int surv_id)
    {
        c = null;
        c = db.rawQuery("select User_ID from Survey where Survey_ID='"+surv_id+"'",null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                String temp = c.getString(c.getColumnIndex("User_ID"));
                return temp;
            }
        }

        return "";
    }

    public Cursor ifSurvInUser(int surv_id)
    {
        c = null;
        c = db.rawQuery("select User_ID from SurvUser where Survey_ID='"+surv_id+"'", null);
        return c;
    }

    public Cursor ifSurvinGrp(int surv_id)
    {
        c = null;
        c = db.rawQuery("select Group_ID from SurvGrp where Survey_ID='"+surv_id+"'", null);
        return c;
    }

    public Cursor getUsersFromGrp(String grp_id)
    {
        c = null;
        c = db.rawQuery("select User_ID from UserGrp where Group_ID='"+grp_id+"'", null);
        return c;

    }

    public Cursor ifSurvinDept(int surv_id)
    {
        c = null;
        c = db.rawQuery("select Dept_ID from SurvDept where Survey_ID='"+surv_id+"'", null);
        return c;

    }

    public String getDeptMail(String dept_id)
    {
        c= null;
        c = db.rawQuery("select Email_ID from Department where Dept_ID='"+dept_id+"'", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                String mail = c.getString(c.getColumnIndex("Email_ID"));
                return mail;
            }
        }
        return "";
    }

    public boolean ifSurvinOrg(int surv_id)
    {
        c = null;
        c = db.rawQuery("select * from SurvOrg where Survey_ID='"+surv_id+"'", null);
        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                return true;
            }
        }

        return false;
    }

    public String getDeadline(int surv_id)
    {
        c = null;
        c = db.rawQuery("select Deadline from Survey where Survey_ID='"+surv_id+"'", null);

        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                String temp = c.getString(c.getColumnIndex("Deadline"));
                return temp;
            }
        }
        return "";
    }

    public String getNameFromSurv(int surv_id)
    {
        c = null;
        c = db.rawQuery("select Name from Survey where Survey_ID='"+surv_id+"'", null);
        if(c!=null && c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                String temp = c.getString(c.getColumnIndex("Name"));
                return temp;
            }
        }

        return "";
    }



}
