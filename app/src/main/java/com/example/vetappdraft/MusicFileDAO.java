package com.example.vetappdraft;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.vetappdraft.MusicFile;
import java.util.List;

@Dao
public interface MusicFileDAO {
    @Insert
    void insert(MusicFile musicFile);

    @Query("SELECT * FROM music_files")
    LiveData<List<MusicFile>> getAllMusicFiles();

    @Query("SELECT * FROM music_files WHERE userId = :userId")
    LiveData<List<MusicFile>> getMusicFilesByUser(int userId);

    @Query("DELETE FROM music_files")
    void deleteAllMusicFiles();
}
