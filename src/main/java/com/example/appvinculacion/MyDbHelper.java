package com.example.appvinculacion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.support.annotation.Nullable;

public class MyDbHelper extends SQLiteOpenHelper {

    public MyDbHelper(Context context,String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)  {
        db.execSQL(MetodosBDD.CREATE_TABLE);
        db.execSQL(MetodosBDD.CREATE_TABLE1);
        db.execSQL(MetodosBDD.CREATE_TABLE2);
        db.execSQL(MetodosBDD.CREATE_TABLE3);

    }

    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + MetodosBDD.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MetodosBDD.TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + MetodosBDD.TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + MetodosBDD.TABLE_NAME3);
        onCreate(db);
    }
}
