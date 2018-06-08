package com.example.songzeceng.studyofipc;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by songzeceng on 2018/6/6.
 */

public class PersonProvider extends ContentProvider {
    private static final String TAG = "PersonProvider";
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String AUTH = "com.example.songzeceng.PersonProvider";
    public static final Uri PERSON_URI = Uri.parse("content://" + AUTH + "/person");
    public static final String TYPE_MORE = "vnd.android.cursor.dir/person"; // 多条查询的type
    public static final String TYPE_SINGAL = "vnd.android.cursor.item/person"; // 单条查询的type
    public static final int CODE_SINGAL = 0; // 单条查询的匹配码
    public static final int CODE_MORE = 1; // 多条查询的匹配码

    public static String TABLE_NAME = "person";

    private SQLiteDatabase mDatabase;
    private Context mContext;

    static {
        MATCHER.addURI(AUTH, "person", CODE_MORE); // 多条查询
        MATCHER.addURI(AUTH, "person/#", CODE_SINGAL); // 单条查询，#是数字通配符，理解为id
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDatabase = new MyDbHelper(mContext).getWritableDatabase();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        System.out.println("query uri:"+uri.toString());
        String type = getType(uri);
        if (isTypeValid(type)) {
            Cursor cursor = mDatabase.query(TABLE_NAME, projection, selection, selectionArgs, null, sortOrder, null);
            return cursor;
        }
        return null;
    }

    private boolean isTypeValid(String type) {
        return type != null && (TYPE_MORE.equals(type) || TYPE_SINGAL.equals(type));
    }

    @Override
    public String getType(Uri uri) {
        int code = MATCHER.match(uri);
        switch (code) {
            case CODE_SINGAL:
                return TYPE_SINGAL;
            case CODE_MORE:
                return TYPE_MORE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        System.out.println("insert uri:"+uri.toString() + "--contentValues:"+values.toString());
        if (isTypeValid(getType(uri))) {
            mDatabase.insert(TABLE_NAME, null, values);
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        System.out.println("delete uri:"+uri.toString());
        if (isTypeValid(getType(uri))) {
            int count = mDatabase.delete(TABLE_NAME, selection, selectionArgs);
            if (count > 0) {
                mContext.getContentResolver().notifyChange(uri ,null);
            }
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        System.out.println("update uri:"+uri.toString() + "--contentValues:"+values.toString());
        if (isTypeValid(getType(uri))) {
            int count = mDatabase.update(TABLE_NAME, values, selection, selectionArgs);
            if (count > 0) {
                mContext.getContentResolver().notifyChange(uri, null);
            }
        }
        return 0;
    }
}
