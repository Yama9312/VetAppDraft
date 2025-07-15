package com.example.vetappdraft;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "image_files")
public class ImageFile {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String fileName;
    private String filePath;
    private long fileSize;  // bytes
    private int width;      // Optional: image dimensions
    private int height;
    private int userId;     // Links to VetUser.UID

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
