package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    Context context;
    private static final String DB_NAME = "mydatabase";
    private static final String TABLE_NAME = "details";
    private static final String COL_0 = "id";
    private static final String COL_1 = "title";
    private static final String COL_2 = "discription";
    private static final String COL_3 = "checked";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME +" ( "+COL_0+" INT PRIMARY KEY , "+COL_1+" BLOB(1000) , "+COL_2+" BLOB(1000) , "+COL_3+" INT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(Row row){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_0,row.id);
        cv.put(COL_1,row._Title);
        cv.put(COL_2,row._Discription);
        cv.put(COL_3,row.checked);
        db.insert(TABLE_NAME,null,cv);
    }


    public ArrayList<Row> getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        ArrayList<Row> ans = new ArrayList<>();

         while (cursor.moveToNext()) {
             Row row = new Row(cursor.getInt(0), cursor.getString(1), cursor.getString(2),cursor.getInt(3));
             ans.add(row);
         }

        return ans;
    }

    public void updataRow(Row row){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_0,row.id);
        cv.put(COL_1,row._Title);
        cv.put(COL_2,row._Discription);
        cv.put(COL_3,row.checked);
        db.update(TABLE_NAME,cv,COL_0+" = "+row.id,null);

    }

    public void deleteRow(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,COL_0 + " = "+id,null);
    }
}
