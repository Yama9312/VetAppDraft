package com.example.vetappdraft;

public class Page {
    private final String mcName;
    private final Class<?> mcActivityClass;
    private final String mcLink;
    private final String mcInstructions;

    // need to change class to fragment
    // after change need to add another fragment option that will route user to second linked step
    // when all items are made vector of Pages will be sorted and stored in the DB
    public Page(String name, Class<?> activityClass, String mcLink, String mcInstructions) {
        this.mcName = name;
        this.mcActivityClass = activityClass;
        this.mcLink = mcLink;
        this.mcInstructions = mcInstructions;
    }

    public String getName() {
        return mcName;
    }
    public String getLink () {
        return mcLink;
    }
    public String getInstructions() {
        return mcInstructions;
    }
    public Class<?> getActivityClass() {
        return mcActivityClass;
    }
}

