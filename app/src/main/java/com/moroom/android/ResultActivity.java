package com.moroom.android;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moroom.android.databinding.ActivityResultBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private ActivityResultBinding binding;
    private RecyclerView.Adapter adapter;
    private final ArrayList<Contents> arrayList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViews(getIntentData());
        setupListeners();
    }

    private String getIntentData() {
        Intent intent = getIntent();
        String address = intent.getStringExtra("searchedAddress");
        return address;
    }

    private void setupViews(String address) {
        setupRecyclerView();
        setReviews(address);
    }

    private void setupRecyclerView() {
        binding.searchedRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.searchedRecyclerView.setLayoutManager(layoutManager);
    }

    private void setReviews(String searchedAddress) {
        binding.tvAddressTitle.setText(searchedAddress);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Address").child(searchedAddress);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                if (snapshot.exists()) loadReviewsFromSnapshot(snapshot);
                else showEmptyReviewMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ResultActivity", String.valueOf(error.toException()));
            }
        });

        setupAdapter();
    }

    private void loadReviewsFromSnapshot(DataSnapshot snapshot) {
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            if (dataSnapshot.getKey().equals("latitude") || dataSnapshot.getKey().equals("longitude")) {
                // 필요없는 "latitude", "longitude" 값은 건너뜀
                continue;
            }
            Contents contents = dataSnapshot.getValue(Contents.class);
            arrayList.add(contents);
            // Log.d("ResultActivity", "DB data : " + dataSnapshot.getValue());
        }
        adapter.notifyDataSetChanged();
    }

    private void showEmptyReviewMessage() {
        // 후기 작성 권유 텍스트 보여줌
        binding.pleaseMessage3.setVisibility(View.VISIBLE);

        // 하단의 BannerImage, footerMessage 숨김
        binding.stripBannerImage.setVisibility(View.GONE);
        binding.linearFooterMessage.setVisibility(View.GONE);
    }

    private void setupAdapter() {
        adapter = new ContentsAdapter(arrayList);
        binding.searchedRecyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.imgBack.setOnClickListener(view -> onBackPressed());
        binding.imgHome.setOnClickListener(view -> navigateToMainActivity(R.id.navigation_home));
        binding.imgDash.setOnClickListener(view -> navigateToMainActivity(R.id.navigation_dashboard));
        binding.imgProfile.setOnClickListener(view -> navigateToMainActivity(R.id.navigation_profile));

        binding.etSearch.setFocusable(false);
        binding.etSearch.setOnClickListener(view -> {
            Intent intent = new Intent(ResultActivity.this, SearchActivity.class);
            getSearchResult.launch(intent);
        });

        binding.fab.setOnClickListener(view -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Intent intent;
            if (user != null) {
                intent = new Intent(ResultActivity.this, WriteActivity.class);
            } else {
                intent = new Intent(ResultActivity.this, MoveToLogin.class);
            }
            startActivity(intent);
        });

        binding.stripBannerImage.setOnClickListener(view -> {
            // 후기 작성 화면으로 이동
            Intent intent = new Intent(ResultActivity.this, WriteActivity.class);
            startActivity(intent);
        });

        binding.moveToWrite.setOnClickListener(view -> {
            // 후기 작성 화면으로 이동
            Intent intent = new Intent(ResultActivity.this, WriteActivity.class);
            startActivity(intent);
        });
    }

    private void navigateToMainActivity(int destination) {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.putExtra("nav_destination", destination);
        startActivity(intent);
    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // setResult에 의해 SearchActivity 로부터의 결과 값이 이곳으로 전달됨.
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        String data = result.getData().getStringExtra("data");

                        Intent intent = new Intent(ResultActivity.this, ResultActivity.class);
                        intent.putExtra("searchedAddress", data);
                        startActivity(intent);

                    }
                }
            }
    );
}