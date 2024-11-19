package com.example.vetappdraft;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VetUser {
    @PrimaryKey (autoGenerate = true)
    private int UID;

    @ColumnInfo (name = "mcBranch")
    private String mcBranch;

    @ColumnInfo (name = "mcEContact")
    private String mcEContact;

    public void setUID(int UID) {
        this.UID = UID;
    }

    public VetUser(String mcBranch, String mcEContact) {
        this.mcBranch = mcBranch;
        this.mcEContact = mcEContact;
    }

    public String getMcEContact() {
        return mcEContact;
    }

    public void setMcEContact(String mcEContact) {
        this.mcEContact = mcEContact;
    }

    public String getMcBranch() {
        return mcBranch;
    }

    public void setMcBranch(String mcBranch) {
        this.mcBranch = mcBranch;
    }

    public int getUID() {
        return UID;
    }
}
