package com.example.capstone;

public class CheckedTextViewData {      // Firebase 의 베이스 리스트 데이터 를 WriteActivity 에서 담는 역할 함 수정 x

    private String listText;

//    private boolean isSelected;

    public CheckedTextViewData(){}

    public String getListText() {
        return listText;
    }

    public void setListText(String listText) {
        this.listText = listText;
    }

//    public boolean isSelected() {
//        return isSelected;
//    }
//
//    public void setSelected(boolean selected) {
//        isSelected = selected;
//    }

}
