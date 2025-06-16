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

    @ColumnInfo(name = "mcMusicPreference")
    private String mcMusicPreference;

    //***************************************************************************
    // Method:      setUID
    //
    // Description: Sets the UID for this VetUser instance
    //
    // Parameters:  UID - the unique identifier to set for the VetUser
    //
    // Returned:    None
    //***************************************************************************
    public void setUID(int UID) {
        this.UID = UID;
    }

    //***************************************************************************
    // Method:      VetUser
    //
    // Description: Constructor to initialize the VetUser instance with branch
    //              and emergency contact information.
    //
    // Parameters:  mcBranch - the branch of the VetUser
    //              mcEContact - the emergency contact for the VetUser
    //
    // Returned:    None
    //***************************************************************************
    public VetUser(String mcBranch, String mcEContact) {
        this.mcBranch = mcBranch;
        this.mcEContact = mcEContact;
        this.mcMusicPreference = null;
    }

    //***************************************************************************
    // Method:      getMcEContact
    //
    // Description: Returns the emergency contact information for the VetUser
    //
    // Parameters:  None
    //
    // Returned:    String - the emergency contact for the VetUser
    //***************************************************************************
    public String getMcEContact() {
        return mcEContact;
    }

    //***************************************************************************
    // Method:      setMcEContact
    //
    // Description: Sets the emergency contact for this VetUser instance
    //
    // Parameters:  mcEContact - the emergency contact to set for the VetUser
    //
    // Returned:    None
    //***************************************************************************
    public void setMcEContact(String mcEContact) {
        this.mcEContact = mcEContact;
    }

    //***************************************************************************
    // Method:      getMcBranch
    //
    // Description: Returns the branch information for the VetUser
    //
    // Parameters:  None
    //
    // Returned:    String - the branch of the VetUser
    //***************************************************************************
    public String getMcBranch() {
        return mcBranch;
    }

    //***************************************************************************
    // Method:      setMcBranch
    //
    // Description: Sets the branch for this VetUser instance
    //
    // Parameters:  mcBranch - the branch to set for the VetUser
    //
    // Returned:    None
    //***************************************************************************
    public void setMcBranch(String mcBranch) {
        this.mcBranch = mcBranch;
    }

    //***************************************************************************
    // Method:      getUID
    //
    // Description: Returns the unique identifier (UID) for the VetUser
    //
    // Parameters:  None
    //
    // Returned:    int - the UID of the VetUser
    //***************************************************************************
    public int getUID() {
        return UID;
    }

    //***************************************************************************
    // Method:      getMusicPreference
    //
    // Description: Returns the music preference string
    //
    // Parameters:  None
    //
    // Returned:    string - the specified music preference
    //***************************************************************************
    public String getMcMusicPreference () {
        return mcMusicPreference;
    }

    //***************************************************************************
    // Method:      setMusicPreference
    //
    // Description: Sets the music preference for the VetUser
    //
    // Parameters:  musicPreference - the string to set for the VetUser music
    //                                preference
    //
    // Returned:    None
    //***************************************************************************
    public void setMcMusicPreference (String mcMusicPreference) {
        this.mcMusicPreference = mcMusicPreference;
    }
}

/* Espi5730 Test Push Again*/
