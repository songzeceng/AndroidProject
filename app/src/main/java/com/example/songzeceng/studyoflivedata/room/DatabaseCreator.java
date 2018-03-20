package com.example.songzeceng.studyoflivedata.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by songzeceng on 2018/3/19.
 */

@Database(entities = {User.class, UserPerforms.class},version = 1,exportSchema = false)
public abstract class DatabaseCreator extends RoomDatabase{
    private static DatabaseCreator databaseCreator;

    public static DatabaseCreator getInstance(Context context){
        if(databaseCreator == null){
            synchronized (DatabaseCreator.class){
                if(databaseCreator == null){
                    databaseCreator = Room.databaseBuilder(context.getApplicationContext(),DatabaseCreator.class,
                            "user_perfom_2018.db").build();
                }
            }
        }
        return databaseCreator;
    }

    public static void onDestroy(){
        databaseCreator = null;
    }

    public abstract CRUDDAO getDao();
}
