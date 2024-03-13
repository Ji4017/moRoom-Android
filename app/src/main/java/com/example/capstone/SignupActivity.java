package com.example.capstone;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.capstone.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    EditText idEditText;
    EditText passwordEditText;
    EditText emailAddressText;
    Button emailSendButton;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleDeepLink();

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idEditText = binding.setId;
        passwordEditText = binding.setPassword;
        emailAddressText = binding.emailAddress;
        emailSendButton = binding.sendEmail;
        signUpButton = binding.register;


        emailSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 아이디 패스워드 형식 검사 후 유저 생성
                if (validateForm()) {
                    createUser(idEditText.getText().toString(), passwordEditText.getText().toString());
                }
            }
        });

        emailAddressText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // 인증용 이메일 필드 초기 셋팅
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    emailAddressText.setText("@cju.ac.kr");
                    emailAddressText.post(new Runnable() {
                        @Override
                        public void run() {
                            emailAddressText.setSelection(0);  // 커서를 맨 앞으로 이동
                        }
                    });
                }

            }
        });

        emailAddressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력 변경 전 호출되는 메서드
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력이 변경될 때 호출되는 메서드
                // 인증용 이메일 형식 검사
                String email = s.toString().trim();
                if (!isValidEmailDomain(email)) {
                    emailSendButton.setEnabled(false);
                    emailAddressText.setError("청주대학교 이메일만 가능합니다.");
                } else {
                    // 유효한 도메인인 경우 에러 제거
                    emailAddressText.setError(null);
                    emailSendButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 입력 변경 후 호출되는 메서드
            }
        });
    }


    private boolean isValidEmailDomain(String email) {
        String domain = email.substring(email.indexOf("@") + 1);
        return domain.equalsIgnoreCase("cju.ac.kr");
    }

    private boolean isValidId(String id) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(id).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    private boolean validateForm() {
        boolean id = isValidId(idEditText.getText().toString());
        boolean password = isValidPassword(passwordEditText.getText().toString());

        if (!id) {
            idEditText.setError("유효하지 않은 이메일 형식입니다.");
        } else {
            idEditText.setError(null);
        }

        if (!password) {
            passwordEditText.setError("비밀번호는 6자리 이상이어야 합니다.");
        } else {
            passwordEditText.setError(null);
        }

        return (id && password);
    }


    private void sendEmail() {
        String email = emailAddressText.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        // firebase dynamic link prefix
                        .setUrl("https://moroom.page.link/m1r2")
                        .setHandleCodeInApp(true)
                        .setAndroidPackageName(
                                "com.example.capstone",
                                false, /* installIfNotAvailable */
                                "12"                  /* minimumVersion */
                        )
                        .build();
        auth.sendSignInLinkToEmail(email, actionCodeSettings).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "이메일을 보냈습니다.");
                    Toast.makeText(getApplicationContext(), "이메일을 보냈습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "이메일 보내기 실패", task.getException());
                    Toast.makeText(getApplicationContext(), "이메일 전송 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleDeepLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        if (pendingDynamicLinkData != null) {
                            Uri deepLink = pendingDynamicLinkData.getLink();
                            if (deepLink != null) {
                                showSignUpForm();
                            }
                        }
                    }
                })
                .addOnFailureListener(this, e -> Log.w(TAG, "getDynamicLink:onFailure", e));
    }
    
    private void createUser(String email, String password) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    // createUser 완료하면 Firebase RealtimeDB에 email, password, uid 저장
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveDataToFirebaseDB(email, password);
                            Log.d("check", "createUserWithEmail:success");
                            Toast.makeText(SignupActivity.this, "반갑습니다", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();


                        } else {
                            Log.e("error", "createUserWithEmail:failure", task.getException());

                            if (task.getException() instanceof FirebaseAuthException) {
                                // FirebaseAuthException authException = (FirebaseAuthException) task.getException();
                                // String errorMessage = authException.getMessage();
                                Toast.makeText(SignupActivity.this, "해당 이메일은 이미 사용 중입니다", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SignupActivity.this, "회원가입 오류입니다 이메일을 다시 확인해주세요", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
    }

    private void saveDataToFirebaseDB(String email, String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(email);
        userAccount.setPassword(password);
        userAccount.setIdToken(uid);
        userAccount.setReview(false);

//        Log.d("check", userAccount.getEmail()+ "\n" + userAccount.getPassword()+ "\n" + userAccount.getIdToken());

        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("UserAccount");
        databaseReference.child(uid).setValue(userAccount, (databaseError, databaseReference1) -> {
            if (databaseError != null) {
                Log.d("check", "데이터 저장 실패: " + databaseError.getMessage());
            } else {
                Log.d("check", "데이터 저장 성공");
            }
        });
    }

    private void showSignUpForm() {
        emailAddressText.setVisibility(View.GONE);
        emailSendButton.setVisibility(View.GONE);

        idEditText.setVisibility(View.VISIBLE);
        passwordEditText.setVisibility(View.VISIBLE);
        signUpButton.setVisibility(View.VISIBLE);
    }
}