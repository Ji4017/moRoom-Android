package com.example.capstone.data;

import com.example.capstone.data.model.LoggedInUser;

import java.io.IOException;

/**
 * 로그인 자격 증명이 있는 인증을 처리하고 사용자 정보를 검색하는 클래스입니다.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");

            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}