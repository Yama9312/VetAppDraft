package com.example.vetappdraft;

public class Page {

    public enum PageType {
        TEXT, AUDIO, LINK, IMAGE // more types to add in future
    }

    private final String mcName;
    private final PageType mcType;
    private final String mcContent;
//    private final Class<?> mcActivityClass;
//    private final String mcLink;
    private final String mcInstructions;

    // need to change class to fragment
    // after change need to add another fragment option that will route user to second linked step
    // when all items are made vector of Pages will be sorted and stored in the DB
//    public Page(String name, Class<?> activityClass, String mcLink, String mcInstructions) {
//        this.mcName = name;
//        this.mcActivityClass = activityClass;
//        this.mcLink = mcLink;
//        this.mcInstructions = mcInstructions;
//    }

    public Page(String name, PageType type, String content, String instructions) {
        this.mcName = name;
        this.mcType = type;
        this.mcContent = content;
        this.mcInstructions = instructions;
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

//    public String getLink () {
//        return mcLink;
//    }

    public String getInstructions() {
        return mcInstructions;
    }

//    public Class<?> getActivityClass() {
//        return mcActivityClass;
//    }
}

