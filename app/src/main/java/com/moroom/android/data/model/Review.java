package com.moroom.android.data.model;

import java.util.HashMap;

public class Review {
    private String title = "";
    private String pros = "";
    private String cons = "";
    private String checkedItems = "";

    public String getTitle() {
        return title;
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

    public String getCheckedItems() { return checkedItems; }

    public void setCheckedItems(String checkedItems) { this.checkedItems = checkedItems; }
}
