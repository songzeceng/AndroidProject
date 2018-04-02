package com.example.songzeceng.studyoflivedata.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.songzeceng.studyoflivedata.entity.User;
import com.example.songzeceng.studyoflivedata.entity.UserPerforms;

/**
 * Created by songzeceng on 2018/3/19.
 */

@Database(entities = {User.class, UserPerforms.class}, version = 1, exportSchema = true)
public abstract class DatabaseCreator extends RoomDatabase {
    private static DatabaseCreator databaseCreator;

    public static DatabaseCreator getInstance(Context context) {
        if (databaseCreator == null) {
            synchronized (DatabaseCreator.class) {
                if (databaseCreator == null) {
                    databaseCreator = Room.databaseBuilder(context.getApplicationContext(), DatabaseCreator.class,
                            "user_perfom_2018_3.db").addMigrations(new Migration(1, 2) {
                        //版本升级.从1升到2
                        @Override
                        public void migrate(@NonNull SupportSQLiteDatabase database) {
                            database.execSQL("create table coach(id int,name varchar(30),age int,constraint pk_coach PRIMARY_KEY(id) )");
                        }
                        //版本升级并不方便，也不提倡频繁对数据库进行组织结构的变动
                    }).build();
                }
            }
        }
        return databaseCreator;
    }

    public static void onDestroy() {
        databaseCreator = null;
    }

    public abstract CRUDDAO getDao();
}
