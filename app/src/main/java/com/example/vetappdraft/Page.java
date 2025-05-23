package com.example.vetappdraft;

public class Page {

    public enum PageType {
        TEXT, AUDIO, LINK, IMAGE // more types to add in future
    }

    private final String mcName;
    private final PageType mcType;
    private final String mcContent;
    private final String mcInstructions;
    private final int audioResID;

    // need to change class to fragment
    // after change need to add another fragment option that will route user to second linked step
    // when all items are made vector of Pages will be sorted and stored in the DB

    public Page(String name, PageType type, String content, String instructions, int audioResID) {
        this.mcName = name;
        this.mcType = type;
        this.mcContent = content;
        this.mcInstructions = instructions;
        this.audioResID = audioResID;
    }
    public Page(String name, PageType type, String content, String instructions) {
        this.mcName = name;
        this.mcType = type;
        this.mcContent = content;
        this.mcInstructions = instructions;
        this.audioResID = -999;
    }

    public String getName() {
        return mcName;
    }

    public PageType getType() {
        return mcType;
    }

    public String getContent() {
        return mcContent;
    }

    public String getInstructions() {
        return mcInstructions;
    }
    public int getAudioResId() {return audioResID;}
}

