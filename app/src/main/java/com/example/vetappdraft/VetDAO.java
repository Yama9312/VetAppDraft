package com.example.vetappdraft;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VetDAO {
    @Query ("SELECT * FROM VetUser")
    List<VetUser> getAll();

    @Insert void insert(VetUser data);

    @Delete
    void delete (VetUser data);

    @Query ("DELETE FROM VetUser")
    void deleteAll();

    @Query ("SELECT COUNT(*) FROM VetUser")
    int getSize ();

    @Update
    void update(VetUser user);

}
