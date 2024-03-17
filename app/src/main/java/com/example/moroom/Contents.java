package com.example.moroom;

import java.util.HashMap;

public class Contents {
    private String title;
    private String goodThing;
    private String badThing;
    private HashMap<String, String> selectedList;


    // 리뷰 작성 여부에 따라 리뷰글을 블러처리 하기 위한 필드
    private String idToken;
    private boolean isBlur;


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

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public boolean isBlur() {
        return isBlur;
    }

    public void setBlur(boolean blur) {
        isBlur = blur;
    }
}
