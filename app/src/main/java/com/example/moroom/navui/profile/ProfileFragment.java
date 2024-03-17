package com.example.moroom.navui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moroom.LoginActivity;
import com.example.moroom.MyReviewActivity;
import com.example.moroom.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private DatabaseReference databaseReference;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private TextView tvUserEmail;
    private TextView tvMyReview;
    private TextView tvLogout;
    private TextView tvDeleteAccount;
    private TextView tvMoRoomVersion;

    private LinearLayout linearTop;
    private LinearLayout linearSeparation1;
    private LinearLayout linearSeparation2;
    private LinearLayout linearSeparation3;
    private LinearLayout linearSeparation4;

    private Button moveToLogin;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tvUserEmail = binding.tvUserEmail;
        tvMyReview = binding.tvMyReview;
        tvLogout = binding.tvLogout;
        tvDeleteAccount = binding.tvDeleteAccount;
        tvMoRoomVersion = binding.tvMoRoomVersion;

        linearTop = binding.linearTop;
        linearSeparation1 = binding.linearSeparation1;
        linearSeparation2 = binding.linearSeparation2;
        linearSeparation3 = binding.linearSeparation3;
        linearSeparation4 = binding.linearSeparation4;

        moveToLogin = binding.moveToLogin;

        if (user != null) {
            // 로그인 된 유저의 `프로필`을 보여줌
            tvUserEmail.setText(user.getEmail());

        } else {
            // 로그인 하러 가기 `버튼`을 보여줌
            goToLogin();
        }

        tvMyReview.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MyReviewActivity.class);
            startActivity(intent);
        });

        tvLogout.setOnClickListener(view -> showLogoutDialog());

        tvDeleteAccount.setOnClickListener(view -> showDeleteDialog());

        moveToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return root;
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("로그아웃");
        builder.setMessage("정말 로그아웃 하시겠습니까?");
        builder.setPositiveButton("확인", (dialogInterface, i) -> logout());
        builder.setNegativeButton("취소", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("회원탈퇴");
        builder.setMessage("정말 회원탈퇴 하시겠습니까?");
        builder.setPositiveButton("확인", (dialogInterface, i) -> deleteAccount());
        builder.setNegativeButton("취소", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void deleteAccount() {
        // delete Authentication data
        user.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

        // delete realTimeDatabase data
        database.getReference("UserAccount").child(user.getUid()).removeValue();
    }

    private void goToLogin() {
        moveToLogin.setVisibility(View.VISIBLE);

        tvUserEmail.setVisibility(View.GONE);
        tvMyReview.setVisibility(View.GONE);
        tvLogout.setVisibility(View.GONE);
        tvDeleteAccount.setVisibility(View.GONE);
        tvMoRoomVersion.setVisibility(View.GONE);
        linearTop.setVisibility(View.GONE);
        linearSeparation1.setVisibility(View.GONE);
        linearSeparation2.setVisibility(View.GONE);
        linearSeparation3.setVisibility(View.GONE);
        linearSeparation4.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}