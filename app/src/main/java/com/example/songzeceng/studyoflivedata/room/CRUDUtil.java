package com.example.songzeceng.studyoflivedata.room;

import android.content.Context;

/**
 * Created by songzeceng on 2018/3/20.
 */

public class CRUDUtil {
    public static CRUDDAO getDao(Context context) {
        return DatabaseCreator.getInstance(context).getDao();
    }
}
