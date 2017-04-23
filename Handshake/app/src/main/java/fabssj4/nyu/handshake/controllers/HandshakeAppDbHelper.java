package fabssj4.nyu.handshake.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by FabSSJ4 on 3/17/2016.
 */
public class HandshakeAppDbHelper {
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "HandshakeAppDb.sqlite";

    private static final int DATABASE_VERSION = 1;

    /*private static final String TABLE_USER = "user";
    private static final String COL_USERCONTACT = "contact";
    private static final String COL_USEREMAIL = "email";
    private static final String COL_USERFIRSTNAME = "firstname";
    private static final String COL_USERLASTNAME = "lastname";
    private static final String COL_USERREGISTRATIONTIMESTAMP = "timestamp";
    private static final String COL_USERREGISTRATIONIP = "ip";*/

    private static final String TABLE_USERCONTACT = "usercontact";
    private static final String COL_USERCONTACT_ID = "contactid";
    private static final String COL_USERCONTACT_CONTACT = "contact";
    private static final String COL_USERCONTACT_NAME = "name";
    private static final String COL_USERCONTACT_LASTSENTTIMESTAMP = "lastsenttimestamp";
    private static final String COL_USERCONTACT_LASTSENTIP = "lastsentip";
    private static final String COL_USERCONTACT_LASTRECEIVEDTIMESTAMP = "lastreceivedtimestamp";
    private static final String COL_USERCONTACT_LASTRECEIVEDIP = "lastreceivedip";

    private static final String TABLE_HSKSETTING = "hsksetting";
    private static final String COL_HSKSETTING_ID = "hsksettingid";
    private static final String COL_HSKSETTING_DURATION = "duration";
    private static final String COL_HSKSETTING_MILLISVIBRATION = "millisvibration";
    private static final String COL_HSKSETTING_PATTERN = "pattern";
    private static final String COL_HSKSETTING_PATTERNDELAY = "patterndelay";
    private static final String COL_HSKSETTING_USEFLASHLIGHT = "useflashlight";

    /*private static final String CREATE_TABLE_USER = "Create table "+TABLE_USER+" ("+
            COL_USERCONTACT+" text not null,"+
            COL_USEREMAIL+" text not null," +
            COL_USERFIRSTNAME+" text not null,"+
            COL_USERLASTNAME+" text not null,"+
            COL_USERREGISTRATIONTIMESTAMP+" text not null,"+
            COL_USERREGISTRATIONIP+" text not null"+
            ");";*/

    private static final String CREATE_TABLE_USERCONTACT = "Create table "+TABLE_USERCONTACT+" ("+
            COL_USERCONTACT_ID+" integer not null,"+
            COL_USERCONTACT_CONTACT+" text not null,"+
            COL_USERCONTACT_NAME+" text not null,"+
            COL_USERCONTACT_LASTSENTTIMESTAMP+" text,"+
            COL_USERCONTACT_LASTSENTIP+" text,"+
            COL_USERCONTACT_LASTRECEIVEDTIMESTAMP+" text,"+
            COL_USERCONTACT_LASTRECEIVEDIP+" text"+
            ");";

    private static final String CREATE_TABLE_HSKSETTING = "Create table "+TABLE_HSKSETTING+" ("+
            COL_HSKSETTING_ID+" integer auto_increment primary key,"+
            COL_HSKSETTING_DURATION+" integer not null,"+
            COL_HSKSETTING_MILLISVIBRATION+" integer not null," +
            COL_HSKSETTING_PATTERN+" integer not null,"+
            COL_HSKSETTING_PATTERNDELAY+" integer not null,"+
            COL_HSKSETTING_USEFLASHLIGHT+" integer not null"+
            ");";

    private class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //db.execSQL(CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_USERCONTACT);
            db.execSQL(CREATE_TABLE_HSKSETTING);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


    public long saveSendHandshakeContact(String contactid,String contact, String name,String ip){
        ContentValues cv = new ContentValues();
        cv.put(COL_USERCONTACT_ID,contactid);
        cv.put(COL_USERCONTACT_CONTACT,contact);
        cv.put(COL_USERCONTACT_NAME,name);
        cv.put(COL_USERCONTACT_LASTSENTIP,ip);
        cv.put(COL_USERCONTACT_LASTSENTTIMESTAMP,String.valueOf(System.currentTimeMillis()));
        return db.insertWithOnConflict(TABLE_USERCONTACT,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
    }

    public long saveReceiveHandshakeContact(String contactid,String contact, String name,String ip){
        ContentValues cv = new ContentValues();
        cv.put(COL_USERCONTACT_ID,contactid);
        cv.put(COL_USERCONTACT_CONTACT,contact);
        cv.put(COL_USERCONTACT_NAME,name);
        cv.put(COL_USERCONTACT_LASTRECEIVEDIP, ip);
        cv.put(COL_USERCONTACT_LASTRECEIVEDTIMESTAMP,String.valueOf(System.currentTimeMillis()));
        return db.insertWithOnConflict(TABLE_USERCONTACT,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
    }

    public LinkedHashMap<Integer,HashMap<String,String>> getRecentSent(){
        String sql = "select "+COL_USERCONTACT_ID+","+COL_USERCONTACT_CONTACT+","+COL_USERCONTACT_NAME+","+COL_USERCONTACT_LASTSENTTIMESTAMP+","+COL_USERCONTACT_LASTSENTIP+" from "+TABLE_USERCONTACT+" order by "+COL_USERCONTACT_LASTSENTTIMESTAMP+" desc";
        Cursor c = db.rawQuery(sql, null);
        LinkedHashMap<Integer,HashMap<String,String>> map = new LinkedHashMap<Integer,HashMap<String, String>>();
        if(c.moveToFirst()){
            do{
                HashMap<String,String> h = new HashMap<String,String>();
                h.put(COL_USERCONTACT_CONTACT,c.getString(c.getColumnIndex(COL_USERCONTACT_CONTACT)));
                h.put(COL_USERCONTACT_NAME,c.getString(c.getColumnIndex(COL_USERCONTACT_NAME)));
                h.put(COL_USERCONTACT_LASTSENTTIMESTAMP,c.getString(c.getColumnIndex(COL_USERCONTACT_LASTSENTTIMESTAMP)));
                h.put(COL_USERCONTACT_LASTSENTIP,c.getString(c.getColumnIndex(COL_USERCONTACT_LASTSENTIP)));
                map.put(c.getInt(c.getColumnIndex(COL_USERCONTACT_ID)),h);
            }while (c.moveToNext());
        }
        return map.isEmpty()?null:map;
    }

    public LinkedHashMap<Integer,HashMap<String,String>> getRecentReceived(){
        String sql = "select "+COL_USERCONTACT_ID+","+COL_USERCONTACT_CONTACT+","+COL_USERCONTACT_NAME+","+COL_USERCONTACT_LASTRECEIVEDTIMESTAMP+","+COL_USERCONTACT_LASTRECEIVEDIP+" from "+TABLE_USERCONTACT+" order by "+COL_USERCONTACT_LASTRECEIVEDTIMESTAMP+" desc";
        Cursor c = db.rawQuery(sql, null);
        LinkedHashMap<Integer,HashMap<String,String>> map = new LinkedHashMap<Integer,HashMap<String, String>>();
        if(c.moveToFirst()){
            int id = c.getInt(c.getColumnIndex(COL_USERCONTACT_ID));
            do{
                HashMap<String,String> h = new HashMap<String,String>();
                h.put(COL_USERCONTACT_CONTACT,c.getString(c.getColumnIndex(COL_USERCONTACT_CONTACT)));
                h.put(COL_USERCONTACT_NAME,c.getString(c.getColumnIndex(COL_USERCONTACT_NAME)));
                h.put(COL_USERCONTACT_LASTRECEIVEDTIMESTAMP,c.getString(c.getColumnIndex(COL_USERCONTACT_LASTRECEIVEDTIMESTAMP)));
                h.put(COL_USERCONTACT_LASTRECEIVEDIP,c.getString(c.getColumnIndex(COL_USERCONTACT_LASTRECEIVEDIP)));
                map.put(c.getInt(c.getColumnIndex(COL_USERCONTACT_ID)),h);
            }while (c.moveToNext());
        }
        return map.isEmpty()?null:map;
    }


    public HandshakeAppDbHelper(Context context){
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

}
