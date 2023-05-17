package com.example.capstone;

import java.util.List;

public class WrittenReviewData {
    private String idToken;
    private String address;
    private String title;
    private String opinion;
    private String selectedListText;

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

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
