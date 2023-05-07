package com.example.capstone;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.capstone.databinding.ActivitySignUpBinding;
import com.example.capstone.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private EditText emailAddressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_sign_up);
        final EditText idEditText = binding.setId;
        final EditText passwordEditText = binding.setPassword;
        emailAddressText = binding.emailAddress;
        final Button emailSendButton = binding.sendEmail;
        final Button signUpButton = binding.register;
        emailSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDeepLink();
                sendEmail();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(idEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

    }

    private void sendEmail(){
        String email = emailAddressText.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl("https://moroom.page.link/m1r2")
                        // This must be true
                        .setHandleCodeInApp(true)
//                    .setIOSBundleId("com.example.ios")
                        .setAndroidPackageName(
                                "com.example.capstone",
                                false, /* installIfNotAvailable */
                                "12"    /* minimumVersion */)
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

    private void handleDeepLink(){
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
//                            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
//                            startActivity(intent);
                        }


                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                        // 여기서 이메일링크 누른 후 앱에서의 동작 작성하는거 맞나?
                        // 사용자 아이디 비밀번호 firebase에 추가하는 작업 후 화면 이동?
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });
    }

    private void createUser(String email, String password){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignupActivity.this, "반갑습니다..", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
//                            FirebaseUser user = auth.getCurrentUser();
//                            updateUI(user);

                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "회원가입 오류.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}