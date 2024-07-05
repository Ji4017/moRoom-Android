package com.moroom.android.data.model;


public class WrittenReviewData {
    private String address;
    private String title;
    private String pros;
    private String cons;
    private String checkedItems;
    private String idToken;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPros() {
        return pros;
    }

    public void setPros(String pros) {
        this.pros = pros;
    }

    public String getCons() {
        return cons;
    }

    public void setCons(String cons) {
        this.cons = cons;
    }

    public String getCheckedItems() {
        return checkedItems;
    }

    public void setCheckedItems(String checkedItems) {
        this.checkedItems = checkedItems;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getTitle() {
        return title;
    }
}
