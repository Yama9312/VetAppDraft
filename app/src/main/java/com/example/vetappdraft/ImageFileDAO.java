package com.example.vetappdraft;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.vetappdraft.ImageFile;
import java.util.List;

@Dao
public interface ImageFileDAO {
    @Insert
    void insert(ImageFile imageFile);

    @Query("SELECT * FROM image_files WHERE userId = :userId")
    List<ImageFile> getImagesByUser(int userId);
}
