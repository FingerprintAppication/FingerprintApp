package com.example.i2ichest_.fingerprintit.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;

import java.util.HashMap;

/**
 * Created by I2ichest_ on 8/2/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "informleave.db";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.getReadableDatabase();
        String sql = "Create Table IF NOT EXISTS Informleave (informID TEXT PRIMARY KEY, stuID TEXT)";
        sqLiteDatabase.execSQL(sql);
        Log.d("SQLITE", "onCreate: CREATED!");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop Table if exists Informleave"); this.onCreate(sqLiteDatabase);

    }

    public boolean addInformRead (InformLeaveModel.InformLeave informLeave) {
        long result = -1;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("informID", informLeave.getInformLeaveID());
            values.put("stuID",informLeave.getStudent().getStudentID().toString());
            result = db.insert("Informleave", null, values);
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

    public HashMap<String,String> getAllInformLeave (){
        HashMap<String,String> list = new HashMap<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select informID,stuID from Informleave", null);
        if(cursor.getCount() !=0){
            cursor.moveToFirst();
            do{
                list.put(cursor.getString(0).toString(),cursor.getString(1).toString());
            }while(cursor.moveToNext());
        }
        db.close();
        return list;

    }

    public boolean deleteInformleave(String id) { long result = -1; try {
        SQLiteDatabase db = this.getWritableDatabase(); // Deleting Row
         result = db.delete("Informleave", "informID = ?", new String[] {id});
        db.close(); // Closing database connection
        }catch (Exception ex) { ex.printStackTrace(); }
        if (result == -1) { return false; }else{ return true; }
    }

    public boolean dropTable (){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table Informleave");
        db.close();
        return true;
    }

    public boolean createTable (){
        SQLiteDatabase db = this.getWritableDatabase();
        this.onCreate(db);
        return true;
    }

}
