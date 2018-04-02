package com.example.songzeceng.studyoflivedata.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.songzeceng.studyoflivedata.entity.User;
import com.example.songzeceng.studyoflivedata.entity.UserPerforms;
import com.example.songzeceng.studyoflivedata.entity.UserSimple;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by songzeceng on 2018/3/19.
 */

@Dao
public interface CRUDDAO {
    @Query("select * from users")
    List<User> getAllUsers();
    //查询方法返回值必须是cursor或arrayList

    @Query("select users.id,users.name from users,performs " +
            "where users.id = performs.p_id and performs.score > :score and performs.assist > :assist")
        // 多表+筛选查询
    List<UserSimple> getUserWithLimits(int score, int assist);

    @Query("select p_id,score,performs.assist from performs " +
            "order by p_id asc")
    List<UserPerforms> getAllPerforms();

    @Query("select * from users")
    Flowable<List<User>> getAllUsersFlowable();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        //插入有冲突，就直接替换
    void insert(User[] users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserPerforms[] performs);

    @Delete
    int delete(User user);
    //删除方法返回值必须是int或void

    @Update
    int update(User user);
    //更新方法返回值也必须是int或void，但不要这么更新，要么用query，指定sql语句，要么直接重新插入
    //实际覆盖插入也可以实现更新

    @Query("update users set name=:name where id = :id")
    void updateName(String name, long id);
}
