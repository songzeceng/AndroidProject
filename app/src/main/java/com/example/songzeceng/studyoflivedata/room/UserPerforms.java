package com.example.songzeceng.studyoflivedata.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * Created by songzeceng on 2018/3/19.
 */

@Entity(tableName = "performs",
        primaryKeys = "p_id",
        foreignKeys = @ForeignKey(entity = User.class
                , parentColumns = "id"
                , childColumns = "p_id")) //定义主键
public class UserPerforms {
    @android.support.annotation.NonNull
    @ColumnInfo(name = "p_id")
    private String p_id;

    @ColumnInfo(name = "score")
    private String score;

    @ColumnInfo(name = "assist")
    private String assist;

    public UserPerforms() {
    }

    public UserPerforms(String p_id, String score, String assist) {
        this.p_id = p_id;
        this.score = score;
        this.assist = assist;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAssist() {
        return assist;
    }

    public void setAssist(String assist) {
        this.assist = assist;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"p_id\":\"")
                .append(p_id).append('\"');
        sb.append(",\"score\":\"")
                .append(score).append('\"');
        sb.append(",\"assist\":\"")
                .append(assist).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
