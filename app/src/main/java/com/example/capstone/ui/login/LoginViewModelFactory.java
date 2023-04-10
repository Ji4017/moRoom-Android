package com.example.capstone.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.example.capstone.data.LoginDataSource;
import com.example.capstone.data.LoginRepository;

/**
 * View 모델 공급자 팩토리에서 LoginView모델을 인스턴스화합니다.
 * 지정된 필수 LoginView모델의 생성자가 비어 있지 않습니다
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(LoginRepository.getInstance(new LoginDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}