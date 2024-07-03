package com.moroom.android.data.model;

public class CheckItem {

    private String listText;

    private boolean isChecked;

    public CheckItem(){}

    public String getListText() {
        return listText;
    }

    public void setListText(String listText) {
        this.listText = listText;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
