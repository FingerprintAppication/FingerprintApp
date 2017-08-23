package com.example.i2ichest_.fingerprintit.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.i2ichest_.fingerprintit.model.AnnouceNewsModel;
import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;

import java.util.HashMap;

/**
 * Created by I2ichest_ on 8/20/2017.
 */

public class AnnounceSqlite extends SQLiteOpenHelper {

    private static String DB_NAME = "announce.db";

    public AnnounceSqlite(Context context) { super(context, DB_NAME, null, 1); }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "Create Table IF NOT EXISTS Announce (announceId TEXT PRIMARY KEY, subject TEXT)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop Table if exists Announce"); this.onCreate(sqLiteDatabase);
    }

    public boolean addAnnounceRead (AnnouceNewsModel.AnnouceNews announces) {
        long result = -1;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("announceId", announces.getAnnouceNewsID());
            values.put("subject",announces.getSchedule().getPeriod().getSection().getSubject().getSubjectName());
            result = db.insert("Announce", null, values);
            db.close();
        }catch(Exception s){
            s.printStackTrace();
        }
        if (result == -1) {
            return false;
        }else{
            return true;
        }
    }

    public HashMap<String,String> getAllAnnounces (){
        HashMap<String,String> list = new HashMap<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select announceId,subject from Announce", null);
        if(cursor.getCount() !=0){
            cursor.moveToFirst();
            do{
                list.put(cursor.getString(0).toString(),cursor.getString(1).toString());
            }while(cursor.moveToNext());
        }
        db.close();
        return list;

    }

    public boolean deleteAnnounce(String id) { long result = -1; try {
        SQLiteDatabase db = this.getWritableDatabase(); // Deleting Row
        result = db.delete("Announce", "announceId = ?", new String[] {id});
        db.close(); // Closing database connection
    }catch (Exception ex) { ex.printStackTrace(); }
        if (result == -1) { return false; }else{ return true; }
    }

}
