package com.example.vetappdraft;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "music_files",
        foreignKeys = @ForeignKey(
                entity = VetUser.class,
                parentColumns = "UID",
                childColumns = "userId",
                onDelete = CASCADE
        )
)
public class MusicFile {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String fileName;
    private String filePath;
    private long fileSize;  // bytes
    private long duration;  // milliseconds

    @ColumnInfo(index = true)
    private int userId;

    // Getters and setters (required by Room)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}