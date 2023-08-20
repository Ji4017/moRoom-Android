package com.example.capstone.navui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.capstone.Contents;
import com.example.capstone.LoginActivity;
import com.example.capstone.MyReviewActivity;
import com.example.capstone.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    final TextView tvUserEmail = binding.tvUserEmail;
    final TextView tvMyReview = binding.tvMyReview;
    final TextView tvLogout = binding.tvLogout;
    final TextView tvWithdrawal = binding.tvWithdrawal;
    final TextView tvMoRoomVersion = binding.tvMoRoomVersion;

    final LinearLayout linearTop = binding.linearTop;
    final LinearLayout linearSeparation1 = binding.linearSeparation1;
    final LinearLayout linearSeparation2 = binding.linearSeparation2;

    final Button moveToLogin = binding.moveToLogin;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (user != null) {
            // 로그인 된 유저면 화면 보여주고
            tvUserEmail.setText(user.getEmail());

        } else {
            // 그렇지 않으면 뷰들 다 숨기고 로그인 화면 이동하기 버튼만 보여줌
            tvUserEmail.setVisibility(View.GONE);
            tvMyReview.setVisibility(View.GONE);
            tvWithdrawal.setVisibility(View.GONE);
            tvLogout.setVisibility(View.GONE);
            tvMoRoomVersion.setVisibility(View.GONE);
            linearTop.setVisibility(View.GONE);
            linearSeparation1.setVisibility(View.GONE);
            linearSeparation2.setVisibility(View.GONE);

            moveToLogin.setVisibility(View.VISIBLE);
        }

        tvMyReview.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MyReviewActivity.class);
            startActivity(intent);
        });

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

    private void ReviewOfCurrentUser(){

    }

    private void CheckReviewExistence() {
        // 현재 유저가 작성한 리뷰가 있는지 없는지 확인

        String uid = user.getUid();
        DatabaseReference userRef = database.getReference("UserAccount").child(uid);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("에러 발생", "Error getting data", task.getException());
                } else {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        Boolean reviewValue = dataSnapshot.child("review").getValue(Boolean.class);

                        Contents contents = new Contents();

                        // 사용자가 작성한 리뷰가 있는 경우 Blur값을 false로
                        // 이거 그냥 if(reviewValue) 로 하면 안되나
                        if (Boolean.TRUE.equals(reviewValue)) {
                            contents.setBlur(false);
                        }

                        // 사용자가 작성한 리뷰가 없는 경우 Blur값을 true로
                        else {
                            contents.setBlur(true);


                        }
                    }
                }
            }
        });

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