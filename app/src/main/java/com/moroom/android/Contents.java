package com.moroom.android;

import java.util.HashMap;

public class Contents {
    private String title;
    private String goodThing;
    private String badThing;
    private HashMap<String, String> selectedList;

    public Contents(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGoodThing() {
        return goodThing;
    }

    public void setGoodThing(String goodThing) {
        this.goodThing = goodThing;
    }

    public String getBadThing() {
        return badThing;
    }

    public void setBadThing(String badThing) {
        this.badThing = badThing;
    }

    public HashMap<String, String> getSelectedList() {
        return selectedList;
    }

    public void setSelectedList(HashMap<String, String> selectedList) { this.selectedList = selectedList; }
}
