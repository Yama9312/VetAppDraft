package com.example.vetappdraft;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {VetUser.class, MusicFile.class, ImageFile.class}, version = 5)
@TypeConverters({Converters.class})
public abstract class VetDatabase extends RoomDatabase {

    //***************************************************************************
    // Method:      vetDAO
    //
    // Description: Provides access to the VetDAO for database operations
    //
    // Parameters:  None
    //
    // Returned:    VetDAO - the DAO for accessing VetUser data
    //***************************************************************************
    public abstract VetDAO vetDAO ();

    //***************************************************************************
    // Method:      musicFileDAO
    //
    // Description: Provides access to the MusicFileDAO for database operations
    //
    // Parameters:  None
    //
    // Returned:    MusicFileDAO - the DAO for accessing music file data
    //***************************************************************************
    public abstract MusicFileDAO musicFileDAO ();

    //***************************************************************************
    // Method:      imageFileDAO
    //
    // Description: Provides access to the imageFileDAO for database operations
    //
    // Parameters:  None
    //
    // Returned:    ImageFileDAO - the DAO for accessing image file data
    //***************************************************************************
    public abstract ImageFileDAO imageFileDAO ();

    //***************************************************************************
    // Method:      getInstance
    //
    // Description: Returns a singleton instance of the VetDatabase
    //
    // Parameters:  context - the application context used to build the database
    //
    // Returned:    VetDatabase - the singleton instance of the database
    //***************************************************************************
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

    public static volatile VetDatabase INSTANCE;
}
