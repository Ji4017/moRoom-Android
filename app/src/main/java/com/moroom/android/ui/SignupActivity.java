package com.moroom.android.ui;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.moroom.android.R;
import com.moroom.android.data.model.UserAccount;
import com.moroom.android.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpListener();
        handleDeepLink();
        initializeEmailField();
        validateEmail();
    }

    private void setUpListener() {
        binding.btSendEmail.setOnClickListener(view -> sendEmail());

        binding.btSignUp.setOnClickListener(view -> {
            if (isValidSignUpForm()) {
                createUser(binding.etId.getText().toString(), binding.etPassword.getText().toString());
            }
        });
    }

    private void initializeEmailField() {
        binding.etEmailAddress.setOnFocusChangeListener((v, hasFocus) -> {
            // 인증용 이메일 필드 초기 셋팅
            if (hasFocus) {
                binding.etEmailAddress.setText("@" + getString(R.string.domain));
                binding.etEmailAddress.post(() -> {
                    // 커서 맨 앞으로 이동
                    binding.etEmailAddress.setSelection(0);
                });
            }
        });
    }

    private void validateEmail() {
        binding.etEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString().trim();
                if (!isValidEmailDomain(email)) {
                    binding.btSendEmail.setEnabled(false);
                    binding.etEmailAddress.setError(getString(R.string.only_cju_email));
                } else {
                    binding.etEmailAddress.setError(null);
                    binding.btSendEmail.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean isValidSignUpForm() {
        boolean isIdValid = isValidId(binding.etId.getText().toString());
        boolean isPasswordValid = isValidPassword(binding.etPassword.getText().toString());

        if (!isIdValid) {
            binding.etId.setError(getString(R.string.invalid_email));
        } else {
            binding.etId.setError(null);
        }

        if (!isPasswordValid) {
            binding.etPassword.setError(getString(R.string.invalid_password));
        } else {
            binding.etPassword.setError(null);
        }

        return (isIdValid && isPasswordValid);
    }

    private boolean isValidEmailDomain(String email) {
        String domain = email.substring(email.indexOf("@") + 1);
        return domain.equalsIgnoreCase(getString(R.string.domain));
    }

    private boolean isValidId(String id) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(id).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    private void sendEmail() {
        String email = binding.etEmailAddress.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        ActionCodeSettings actionCodeSettings = buildActionCodeSettings();
        auth.sendSignInLinkToEmail(email, actionCodeSettings).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Log.d(TAG, "이메일을 보냈습니다.");
                Toast.makeText(getApplicationContext(), "이메일을 보냈습니다", Toast.LENGTH_SHORT).show();
            } else {
                // Log.e(TAG, "이메일 보내기 실패", task.getException());
                Toast.makeText(getApplicationContext(), "이메일 전송 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ActionCodeSettings buildActionCodeSettings() {
        return ActionCodeSettings.newBuilder()
                // URL you want to redirect back to. The domain (www.example.com) for this
                // URL must be whitelisted in the Firebase Console.
                // firebase dynamic link prefix
                .setUrl("https://moroom.page.link/m1r2")
                .setHandleCodeInApp(true)
                .setAndroidPackageName(
                        "com.moroom.android",
                        false, /* installIfNotAvailable */
                        "12"                  /* minimumVersion */
                )
                .build();
    }

    private void handleDeepLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    if (pendingDynamicLinkData != null) {
                        Uri deepLink = pendingDynamicLinkData.getLink();
                        if (deepLink != null) {
                            showSignUpForm();
                        }
                    }
                })
                .addOnFailureListener(this, e -> Log.e(TAG, "getDynamicLink:onFailure", e));
    }

    private void createUser(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Firebase Authentication에 유저 계정 생성
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Firebase DB에 유저 계정 생성
                        createUserInDB(email, password);
                        Toast.makeText(SignupActivity.this, getString(R.string.welcome), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        if (task.getException() instanceof FirebaseAuthException) {
                            // FirebaseAuthException authException = (FirebaseAuthException) task.getException();
                            // String errorMessage = authException.getMessage();
                            Toast.makeText(SignupActivity.this, getString(R.string.in_use_email), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignupActivity.this, getString(R.string.signUp_error), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void createUserInDB(String email, String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("UserAccount");
        assert user != null;
        String uid = user.getUid();

        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(email);
        userAccount.setPassword(password);
        userAccount.setIdToken(uid);
        userAccount.setReview(false);
        databaseReference.child(uid).setValue(userAccount, (databaseError, reference) -> {
            if (databaseError != null) {
                // Log.d(TAG, "유저 DB 저장 실패: " + databaseError.getMessage());
            } else {
                // Log.d(TAG, "유저 DB 저장 성공");
            }
        });
    }

    private void showSignUpForm() {
        binding.etEmailAddress.setVisibility(View.GONE);
        binding.btSendEmail.setVisibility(View.GONE);
        binding.etId.setVisibility(View.VISIBLE);
        binding.etPassword.setVisibility(View.VISIBLE);
        binding.btSignUp.setVisibility(View.VISIBLE);
    }
}