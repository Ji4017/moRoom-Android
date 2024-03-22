package com.moroom.android;


public class WrittenReviewData {
    private String idToken;
    private String address;
    private String title;
    private String selectedListText;
    private String goodThing;
    private String badThing;

    public WrittenReviewData(){}

    public String getSelectedListText() {
        return selectedListText;
    }

    public void setSelectedListText(String selectedListText) {
        this.selectedListText = selectedListText;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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
}
