package com.example.capstone.navui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.capstone.LoginActivity;
import com.example.capstone.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView tvUserEmail = binding.tvUserEmail;
        final TextView tvLogout = binding.tvLogout;
        final TextView tvMoRoomVersion = binding.tvMoRoomVersion;

        final LinearLayout linearTop = binding.linearTop;
        final LinearLayout linearSeparation1 = binding.linearSeparation1;
        final LinearLayout linearSeparation2 = binding.linearSeparation2;

        final Button moveToLogin = binding.moveToLogin;


        // 로그인 된 유저면 화면 보여주고 그렇지 않으면 로그인 화면 이동하기 버튼 보여줌
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvUserEmail.setText(getUserEmail());
        } else {
            tvUserEmail.setVisibility(View.GONE);
            tvLogout.setVisibility(View.GONE);
            tvMoRoomVersion.setVisibility(View.GONE);
            linearTop.setVisibility(View.GONE);
            linearSeparation1.setVisibility(View.GONE);
            linearSeparation2.setVisibility(View.GONE);

            moveToLogin.setVisibility(View.VISIBLE);
        }


        tvLogout.setOnClickListener(view -> {
            showLogoutConfirmationDialog();
        });


        moveToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return root;


    }

    private String getUserEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        return email;
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("로그아웃");
        builder.setMessage("정말로 로그아웃 하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logout();
            }
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}