package com.moroom.android;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.moroom.android.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signUpTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usernameEditText = binding.username;
        passwordEditText = binding.password;
        loginButton = binding.login;
        signUpTextView = binding.signUp;

        loginButton.setEnabled(false);

        signUpTextView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> signIn(usernameEditText.getText().toString(), passwordEditText.getText().toString()));


        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = charSequence.toString().trim();

                if (isValidEmailFormat(email)) {
                    usernameEditText.setError(null);
                    checkSignUpButtonVisibility();
                } else {
                    usernameEditText.setError(getString(R.string.invalid_email));
                    loginButton.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString().trim();
                if (password.length() >= 6) {
                    passwordEditText.setError(null);
                    checkSignUpButtonVisibility();
                } else {
                    passwordEditText.setError(getString(R.string.invalid_password));
                    loginButton.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }


    private void signIn(String email, String password){
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

    private void checkSignUpButtonVisibility() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // ID와 Password에 모두 값이 있고 유효한 이메일 형식인 경우 버튼 활성화
        // 그 외의 경우 버튼 비활성화
        loginButton.setEnabled(!username.isEmpty() && !password.isEmpty() && isValidEmailFormat(username));
    }

    private boolean isValidEmailFormat(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}