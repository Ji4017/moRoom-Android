package com.moroom.android.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.moroom.android.R;
import com.moroom.android.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.moroom.android.ui.navui.MainActivity;
import com.moroom.android.ui.signup.SignupActivity;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpListener();
        validateId();
        validatePassword();
    }

    private void validateId() {
        binding.etId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = charSequence.toString().trim();

                if (isValidEmailFormat(email)) {
                    binding.etId.setError(null);
                    updateLoginButtonState();
                } else {
                    binding.etId.setError(getString(R.string.invalid_email));
                    binding.btLogin.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void validatePassword() {
        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString().trim();
                if (password.length() >= 6) {
                    binding.etPassword.setError(null);
                    updateLoginButtonState();
                } else {
                    binding.etPassword.setError(getString(R.string.invalid_password));
                    binding.btLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void signIn(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        // Log.d(TAG, "signInWithEmail:success");
                        Toast.makeText(LoginActivity.this, R.string.welcome, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        // Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateLoginButtonState() {
        String username = binding.etId.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        binding.btLogin.setEnabled(!username.isEmpty() && !password.isEmpty() && isValidEmailFormat(username));
    }

    private boolean isValidEmailFormat(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void setUpListener() {
        binding.btLogin.setEnabled(false);
        binding.btLogin.setOnClickListener(view -> signIn(binding.etId.getText().toString(), binding.etPassword.getText().toString()));

        binding.tvSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        });
    }
}