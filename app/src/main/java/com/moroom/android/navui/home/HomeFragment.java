package com.moroom.android.navui.home;


import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moroom.android.ContentsAdapter;
import com.moroom.android.MoveToLogin;
import com.moroom.android.R;
import com.moroom.android.Contents;
import com.moroom.android.ResultActivity;
import com.moroom.android.SearchActivity;
import com.moroom.android.WriteActivity;
import com.moroom.android.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView.Adapter adapter;
    private final ArrayList<Contents> arrayList = new ArrayList<>();
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupViews();
        setupListener();

        return view;
    }

    private void setupViews() {
        setupHomeImg();
        setupRecyclerView();
        setupMainReviews();
    }

    private void setupHomeImg() {
        binding.imgHome.setClipToOutline(true);
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.homeRecyclerView.setHasFixedSize(true);
        binding.homeRecyclerView.setLayoutManager(layoutManager);
    }

    private void setupListener() {
        binding.etSearch.setFocusable(false);
        binding.etSearch.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            getSearchResult.launch(intent);
        });

        binding.fab.setOnClickListener(view12 -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Intent intent;

            if (user != null) intent = new Intent(getActivity(), WriteActivity.class);
            else intent = new Intent(getActivity(), MoveToLogin.class);

            startActivity(intent);
        });

        binding.linearUam.setOnClickListener(view -> navigateToDormitory("충북 청주시 청원구 안덕벌로19번길 116 (내덕동) 우암마을"));
        binding.linearYeJi.setOnClickListener(view -> navigateToDormitory("충북 청주시 청원구 안덕벌로19번길 116 (내덕동) 예지관"));
        binding.linearGukJe.setOnClickListener(view -> navigateToDormitory("충북 청주시 청원구 안덕벌로19번길 116 (내덕동) 국제학사"));
        binding.linearJinWon.setOnClickListener(view -> navigateToDormitory("충북 청주시 청원구 수암로66번길 48-2 (우암동, 한진 신세대 아파트)"));
    }

    private void setupMainReviews() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Address").child("reviewForMain");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Contents contents = dataSnapshot.getValue(Contents.class);
                    arrayList.add(contents);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                 Log.e("HomeFragment", String.valueOf(error.toException()));
            }
        });

        setupAdapter();
    }

    private void setupAdapter() {
        adapter = new ContentsAdapter(arrayList, getActivity());
        binding.homeRecyclerView.setAdapter(adapter);
    }

    private void navigateToDormitory(String address) {
        Intent intent = new Intent(getActivity(), ResultActivity.class);
        intent.putExtra("searchedAddress", address);
        startActivity(intent);
    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // setResult에 의해 SearchActivity 로부터의 결과 값이 이곳으로 전달됨.
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        String data = result.getData().getStringExtra("data");

                        Intent intent = new Intent(getActivity(), ResultActivity.class);
                        intent.putExtra("searchedAddress", data);
                        startActivity(intent);

                    }
                }
            }
    );

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}