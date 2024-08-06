package com.moroom.android.presentation.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.moroom.android.databinding.ActivityMoveToLoginBinding;

public class MoveToLogin extends AppCompatActivity {

    private ActivityMoveToLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoveToLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.moveToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(MoveToLogin.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
}