package com.moroom.android.navui.profile;

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

import com.moroom.android.LoginActivity;
import com.moroom.android.MyReviewActivity;
import com.moroom.android.R;
import com.moroom.android.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupView();
        setupListener();

        return view;
    }

    private void setupView() {
        if (user != null) showUserProfile();
        else goToLogin();
    }

    private void showUserProfile() {
        // 로그인 된 유저의 `프로필`을 보여줌
        binding.tvUserEmail.setText(user.getEmail());
    }

    private void goToLogin() {
        binding.moveToLogin.setVisibility(View.VISIBLE);

        binding.tvUserEmail.setVisibility(View.GONE);
        binding.tvMyReview.setVisibility(View.GONE);
        binding.tvLogout.setVisibility(View.GONE);
        binding.tvDeleteAccount.setVisibility(View.GONE);
        binding.tvMoRoomVersion.setVisibility(View.GONE);
        binding.linearTop.setVisibility(View.GONE);
        binding.linearSeparation1.setVisibility(View.GONE);
        binding.linearSeparation2.setVisibility(View.GONE);
        binding.linearSeparation3.setVisibility(View.GONE);
        binding.linearSeparation4.setVisibility(View.GONE);
    }

    private void setupListener() {
        binding.tvMyReview.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyReviewActivity.class);
            startActivity(intent);
        });

        binding.tvLogout.setOnClickListener(v -> showLogoutDialog());

        binding.tvDeleteAccount.setOnClickListener(v -> showDeleteDialog());

        binding.moveToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.log_out));
        builder.setMessage(getString(R.string.log_out_box));
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> logout());
        builder.setNegativeButton(getString(R.string.no), null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.delete_account));
        builder.setMessage(getString(R.string.delete_account_box));
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> deleteAccount());
        builder.setNegativeButton(getString(R.string.no), null);
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
                        Toast.makeText(getActivity(), getString(R.string.delete_complete), Toast.LENGTH_SHORT).show();
                    }
                });

        database.getReference("UserAccount").child(user.getUid()).removeValue();
        database.getReference("Address").child(user.getUid()).getParent().removeValue();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}