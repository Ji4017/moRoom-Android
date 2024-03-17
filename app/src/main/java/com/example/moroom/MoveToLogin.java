package com.example.moroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.moroom.databinding.ActivityMoveToLoginBinding;

public class MoveToLogin extends AppCompatActivity {

    private ActivityMoveToLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoveToLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button moveToLogin = binding.moveToLogin;

        moveToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(MoveToLogin.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
}