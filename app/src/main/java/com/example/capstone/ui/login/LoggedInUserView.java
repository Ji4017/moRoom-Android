package com.example.capstone.ui.login;

/**
 * 인증된 사용자 세부 정보를 UI에 노출하는 클래스입니다.
 */
class LoggedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}