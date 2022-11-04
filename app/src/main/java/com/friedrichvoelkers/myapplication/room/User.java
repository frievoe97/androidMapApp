package com.friedrichvoelkers.myapplication.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public int user_id;

    @ColumnInfo(name = "nickname")
    public String nickname;

    @ColumnInfo(name = "is_mr_x")
    public boolean isMrX;

    @ColumnInfo(name = "is_gamemaster")
    public boolean isGameMaster;
}
