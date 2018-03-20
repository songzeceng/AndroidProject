package com.example.songzeceng.studyoflivedata.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.support.annotation.IntDef;

/**
 * Created by songzeceng on 2018/3/19.
 */

@Entity(tableName = "performs",
        primaryKeys = "p_id",
        foreignKeys = @ForeignKey(entity = User.class
                , parentColumns = "id"
                , childColumns = "p_id"
                , onUpdate = ForeignKey.CASCADE
                , onDelete = ForeignKey.CASCADE)) //定义主键
public class UserPerforms {
    @android.support.annotation.NonNull //
    @ColumnInfo(name = "p_id")
    private long p_id;

    @ColumnInfo(name = "score")
    private int score;

    @ColumnInfo(name = "assist")
    private int assist;

    @Ignore
    public UserPerforms() {
    }

    public UserPerforms(long p_id, int score, int assist) {
        this.p_id = p_id;
        this.score = score;
        this.assist = assist;
    }

    public long getP_id() {
        return p_id;
    }

    public void setP_id(long p_id) {
        this.p_id = p_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getAssist() {
        return assist;
    }

    public void setAssist(int assist) {
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
