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

import com.moroom.android.databinding.ActivitySearchedBinding;
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

public class SearchedActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private RecyclerView.Adapter adapter;
    private final ArrayList<Contents> arrayList = new ArrayList<>();
    public String searchedAddress;
    private EditText searchView;
    private RecyclerView recyclerView;
    private LinearLayout linearFooterMessage, linearNavIcon;
    private FloatingActionButton fab;
    private ImageView stripBannerImage, blurView;
    private TextView addressTitle, pleaseMessage1, pleaseMessage2, pleaseMessage3;
    private Button moveToWrite;
    private ImageButton imgBack, imgHome, imgDash, imgProfile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.moroom.android.databinding.ActivitySearchedBinding binding = ActivitySearchedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        searchView = binding.etAddress;
        addressTitle = binding.tvAddressTitle;

        recyclerView = binding.searchedRecyclerView;
        recyclerView.setHasFixedSize(true); // 아이템이 추가, 삭제, 변경되면 사이즈가 변경될 수 있는 뷰이므로 사이즈 고정해서 불필요한 리소스 아낌
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        imgBack = binding.imgBack;
        imgHome = binding.imgHome;
        imgDash = binding.imgDash;
        imgProfile = binding.imgProfile;

        fab = binding.fab;
        stripBannerImage = binding.stripBannerImage;
        linearFooterMessage = binding.linearFooterMessage;
        linearNavIcon = binding.linearNavIcon;


        blurView = binding.blurView;
        pleaseMessage1 = binding.pleaseMessage1;
        pleaseMessage2 = binding.pleaseMessage2;
        pleaseMessage3 = binding.pleaseMessage3;
        moveToWrite = binding.moveToWrite;


        imgBack.setOnClickListener(view -> {
            // 뒤로 가기
            onBackPressed();
        });

        imgHome.setOnClickListener(view -> {
            Intent intent = new Intent(SearchedActivity.this, MainActivity.class);
            intent.putExtra("selectedFragment", "home"); // 선택한 프래그먼트를 전달
            startActivity(intent);
            finish();
        });

        imgDash.setOnClickListener(view -> {
            Intent intent = new Intent(SearchedActivity.this, MainActivity.class);
            intent.putExtra("selectedFragment", "dashboard"); // 선택한 프래그먼트를 전달
            startActivity(intent);
            finish();
        });

        imgProfile.setOnClickListener(view -> {
            Intent intent = new Intent(SearchedActivity.this, MainActivity.class);
            intent.putExtra("selectedFragment", "profile"); // 선택한 프래그먼트를 전달
            startActivity(intent);
            finish();
        });



        searchView.setFocusable(false);
        searchView.setOnClickListener(view1 -> {
            // 주소 검색 화면으로 이동
            Intent intent = new Intent(SearchedActivity.this, SearchActivity.class);
            getSearchResult.launch(intent);
        });

        fab.setOnClickListener(view -> {
            Intent intent;
            if (user != null) {
                intent = new Intent(SearchedActivity.this, WriteActivity.class);
            } else {
                intent = new Intent(SearchedActivity.this, MoveToLogin.class);
            }
            startActivity(intent);

        });

        stripBannerImage.setOnClickListener(view -> {
            // 후기 작성 화면으로 이동
            Intent intent = new Intent(SearchedActivity.this, WriteActivity.class);
            startActivity(intent);
        });

        moveToWrite.setOnClickListener(view -> {
            // 후기 작성 화면으로 이동
            Intent intent = new Intent(SearchedActivity.this, WriteActivity.class);
            startActivity(intent);
        });

        // 사용자가 검색한 주소명 받아오기
        Intent intent = getIntent();
        searchedAddress = intent.getStringExtra("searchedAddress");

        // 유저가 작성한 리뷰가 존재하는지 확인 후 그에 따라 리뷰작성 화면을 보여주거나 디비에 있는 리뷰를 보여줌
        // CheckReviewExistence();
        ShowReview();
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
                        if (Boolean.TRUE.equals(reviewValue)) {
                            contents.setBlur(false);
                            ShowReview();
                        }

                        // 사용자가 작성한 리뷰가 없는 경우 Blur값을 true로
                        else {
                            contents.setBlur(true);
                            addressTitle.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            fab.setVisibility(View.GONE);
                            blurView.setVisibility(View.VISIBLE);
                            pleaseMessage1.setVisibility(View.VISIBLE);
                            pleaseMessage2.setVisibility(View.VISIBLE);
                            moveToWrite.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

    }


    private void ShowReview() {
        // 디비에 있는 리뷰를 보여줌

        addressTitle.setText(searchedAddress);

        DatabaseReference databaseReference = database.getReference("Address").child(searchedAddress);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                if (snapshot.exists()) {
                    // 해당 주소의 후기가 있을 때
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getKey().equals("latitude") || dataSnapshot.getKey().equals("longitude")) {
                            // 필요없는 "latitude", "longitude" 값은 건너뜀
                            continue;
                        }

                        Contents contents = dataSnapshot.getValue(Contents.class);
                        arrayList.add(contents);


                        // DB에 데이터 잘 받아 오는지 로그로 출력
                        // Log.d("SearchedActivity", "DB data : " + dataSnapshot.getValue());
                    }
                    adapter.notifyDataSetChanged();

                } else {
                    // 해당 주소의 후기가 없을 때 후기 작성 권유 텍스트 보여줌
                    pleaseMessage3.setVisibility(View.VISIBLE);

                    // 하단의 BannerImage, footerMessage 숨김
                    stripBannerImage.setVisibility(View.GONE);
                    linearFooterMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SearchedActivity", String.valueOf(error.toException()));
            }
        });

        adapter = new ContentsAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);
    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // setResult에 의해 SearchActivity 로부터의 결과 값이 이곳으로 전달됨.
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        String data = result.getData().getStringExtra("data");

                        Intent intent = new Intent(SearchedActivity.this, SearchedActivity.class);
                        intent.putExtra("searchedAddress", data);
                        startActivity(intent);

                    }
                }
            }
    );


}