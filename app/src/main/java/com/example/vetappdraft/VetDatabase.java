package com.example.vetappdraft;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {VetUser.class}, version = 2)
public abstract class VetDatabase extends RoomDatabase {
    public abstract VetDAO vetDAO ();

    public static volatile VetDatabase INSTANCE;

    public static VetDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (VetDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    VetDatabase.class, "Vet-DB")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
