package com.example.capstone;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idEditText = binding.setId;
        passwordEditText = binding.setPassword;
        emailAddressText = binding.emailAddress;
        emailSendButton = binding.sendEmail;
        signUpButton = binding.register;

        // 폼 양식에 맞게 입력 했을 때 true로 함
        emailSendButton.setEnabled(false);
        signUpButton.setEnabled(false);

        // 사용자가 이메일로부터 받은 인증 링크를 눌렀을 때 보이게 할 예정
        idEditText.setVisibility(View.GONE);
        passwordEditText.setVisibility(View.GONE);
        signUpButton.setVisibility(View.GONE);

        // 인증링크를 눌렀을 때 회원가입 폼이 보이게 함.
        boolean showForm = getIntent().getBooleanExtra("showSignupForm", false);
        if (showForm){
            emailAddressText.setVisibility(View.GONE);
            emailSendButton.setVisibility(View.GONE);

            idEditText.setVisibility(View.VISIBLE);
            passwordEditText.setVisibility(View.VISIBLE);
            signUpButton.setVisibility(View.VISIBLE);
        }

        emailSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(idEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        // 인증용 이메일 텍스트필드 초기 셋팅
        emailAddressText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
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


        // 인증용 이메일 텍스트필드 이메일 검증 및 제어
        emailAddressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력 변경 전 호출되는 메서드
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력이 변경될 때 호출되는 메서드
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


        // ID용 Email 형식 검사
        idEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력 변경 전 호출되는 메서드
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력이 변경될 때 호출되는 메서드
                String email = s.toString().trim();
                if (isValidEmailFormat(email)) {
                    idEditText.setError(null);  // 유효한 형식인 경우 에러 제거
                    checkSignUpButtonVisibility();
                } else {
                    idEditText.setError("유효하지 않은 이메일 형식입니다.");
                    signUpButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 입력 변경 후 호출되는 메서드
            }
        });

        // Password 길이 검사
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력 변경 전 호출되는 메서드
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력이 변경될 때 호출되는 메서드
                String password = s.toString().trim();
                if (password.length() >= 6) {
                    passwordEditText.setError(null);  // 6자리 이상인 경우 에러 제거
                    checkSignUpButtonVisibility();
                } else {
                    passwordEditText.setError("비밀번호는 6자리 이상이어야 합니다.");
                    signUpButton.setEnabled(false);
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

    private boolean isValidEmailFormat(String email) {
        // 정규식 사용해서 검사도 가능
        // return email.matches("이메일_정규식_표현");
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void checkSignUpButtonVisibility() {
        if (idEditText.getError() == null && passwordEditText.getError() == null) {
            signUpButton.setEnabled(true);  // ID와 Password에 모두 에러가 없는 경우 버튼 활성화
        } else {
            signUpButton.setEnabled(false);  // 에러가 있는 경우 버튼 비활성화
        }
    }

    private void sendEmail(){
        String email = emailAddressText.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        // firebase 다이나믹 링크 prefix
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
                }
                else{
                    Log.e(TAG, "이메일 보내기 실패", task.getException());
                    Toast.makeText(getApplicationContext(), "이메일 전송 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createUser(String email, String password){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    // createUser 완료하면 Firebase RealtimeDB에 email, password, uid 저장
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveDataToFirebaseDB(email, password, auth);
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignupActivity.this, "반갑습니다..", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();


                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            if (task.getException() instanceof FirebaseAuthException) {
                                FirebaseAuthException authException = (FirebaseAuthException) task.getException();
//                                String errorMessage = authException.getMessage();
                                Toast.makeText(SignupActivity.this, "해당 이메일은 이미 사용 중입니다", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(SignupActivity.this, "회원가입 오류입니다 이메일을 다시 확인해주세요", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
    }
    private void saveDataToFirebaseDB(String email, String password, FirebaseAuth auth){
        FirebaseUser firebaseUser = auth.getCurrentUser();

        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(firebaseUser.getEmail());
        userAccount.setPassword(password);
        userAccount.setIdToken(firebaseUser.getUid());
        userAccount.setReview(false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("UserAccount");
        databaseReference.child(firebaseUser.getUid()).setValue(userAccount);
    }
}