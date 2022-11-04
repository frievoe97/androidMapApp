package com.friedrichvoelkers.myapplication.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Game {
    @PrimaryKey
    public int game_id;

    @ColumnInfo(name = "duration")
    public int duration;

    @ColumnInfo(name = "interval")
    public int interval;
}
