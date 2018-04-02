package com.example.songzeceng.studyoflivedata.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

/**
 * Created by songzeceng on 2018/3/19.
 */

@Entity(tableName = "users", //表名
        primaryKeys = {"id", "name"},//主键
        indices = {//索引
                @Index(value = "id", unique = true) //唯一性
        }) //实体
public class User implements Comparable {
    @android.support.annotation.NonNull
    @ColumnInfo(name = "id")
    private long id;

    @android.support.annotation.NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "position")
    private String position;

    @Embedded
    private UserPerforms performs;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"position\":\"")
                .append(position).append('\"');
        if (performs != null) {
            sb.append(",\"performs\":")
                    .append(performs.toString());
        }
        sb.append('}');
        return sb.toString();
    }

    @Ignore
    public User() {
    }

    public User(long id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public UserPerforms getPerforms() {
        return performs;
    }

    public void setPerforms(UserPerforms performs) {
        this.performs = performs;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof User) {
            User u = (User) o;
            if (this.id > u.id) {
                return 1;
            } else if (this.id < u.id) {
                return -1;
            } else if (this.id == u.id) {
                return 0;
            }
        }
        return 0;
    }
}
