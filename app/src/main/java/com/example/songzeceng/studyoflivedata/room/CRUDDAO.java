package com.example.songzeceng.studyoflivedata.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by songzeceng on 2018/3/19.
 */

@Dao
public interface CRUDDAO {
    @Query("select * from users")
    List<User> getAllUsers();
    //查询方法返回值必须是cursor或arrayList

    @Query("select users.id,users.name from users,performs" +
            " where users.id = performs.p_id and performs.score > :score and performs.assist > :assist")
    // 多表+筛选查询
    List<UserSimple> getUserWithLimits(int score,int assist);

    @Insert(onConflict = OnConflictStrategy.REPLACE) //插入有冲突，就直接替换
    void insert(User[] users);

    @Delete
    int delete(User user);
    //删除方法返回值必须是int或void

    @Update
    int update(User user);
    //更新方法返回值也必须是int或void
}
