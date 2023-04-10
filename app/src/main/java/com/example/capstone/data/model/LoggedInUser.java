package com.example.capstone.data.model;

/**
 * Login Repository에서 검색한 로그인한 사용자의 사용자 정보를 캡처하는 데이터 클래스
 * Id와 displayName만 저장하고 있는데 닉네임이나 토큰같은 것들을 확장하면 됨
 */
public class LoggedInUser {

    private String userId;
    private String displayName;

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}