package com.example.songzeceng.studyofipc;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by songzeceng on 2018/6/6.
 */

public class MyDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "peopleDatabase.db";
    public static final String TABLE_NAME = "person";
    public static final int VERSION = 1;

    public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        this(context);
    }

    public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        this(context);
    }

    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TABLE_NAME + "(id int(3) not null, name varchar(20), description varchar(50), " +
                                "constraint PK_PERSON primary key(id))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
