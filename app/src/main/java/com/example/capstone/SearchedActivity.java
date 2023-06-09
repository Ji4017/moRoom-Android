package com.example.capstone;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.databinding.ActivitySearchedBinding;
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
    private TextView addressTitle;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ImageView stripBannerImage;
    private ImageView blurView;
    private TextView pleaseMessage1;
    private TextView pleaseMessage2;
    private TextView pleaseMessage3;
    private Button moveToWrite;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.capstone.databinding.ActivitySearchedBinding binding = ActivitySearchedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        searchView = binding.etAddress;
        addressTitle = binding.tvAddressTitle;

        recyclerView = binding.searchedRecyclerView;
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fab = binding.fab;
        stripBannerImage = binding.stripBannerImage;

        blurView = binding.blurView;
        pleaseMessage1 = binding.pleaseMessage1;
        pleaseMessage2 = binding.pleaseMessage2;
        pleaseMessage3 = binding.pleaseMessage3;
        moveToWrite = binding.moveToWrite;


        searchView.setFocusable(false);
        searchView.setOnClickListener(view1 -> {
            Intent intent = new Intent(SearchedActivity.this, SearchActivity.class);
            getSearchResult.launch(intent);
        });


        fab.setOnClickListener(view -> {
            Intent intent = new Intent(SearchedActivity.this, WriteActivity.class);
            startActivity(intent);

        });

        stripBannerImage.setOnClickListener(view -> {
            Intent intent = new Intent(SearchedActivity.this, WriteActivity.class);
            startActivity(intent);
        });


        moveToWrite.setOnClickListener(view -> {
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

    private void CheckReviewExistence(){
        // 현재 유저가 작성한 리뷰가 있는지 없는지 확인

        String uid = user.getUid();

        DatabaseReference userRef = database.getReference("UserAccount").child(uid);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("에러 발생", "Error getting data", task.getException());
                }
                else {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        Boolean reviewValue = dataSnapshot.child("review").getValue(Boolean.class);

                        Contents contents = new Contents();

                        // 사용자가 작성한 리뷰가 있는 경우 Blur값을 false로
                        if (reviewValue == true){
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


    private void ShowReview(){
        // 디비에 있는 리뷰를 보여줌

        addressTitle.setText(searchedAddress);

        DatabaseReference databaseReference = database.getReference("Address").child(searchedAddress);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                if (snapshot.exists()){
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

                }

                else{
                    // 해당 주소의 후기가 없을 때 후기 작성 권유 텍스트 보여줌
                    pleaseMessage3.setVisibility(View.VISIBLE);
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
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        String data = result.getData().getStringExtra("data");

                        Intent intent = new Intent(SearchedActivity.this, SearchedActivity.class);
                        intent.putExtra("searchedAddress", data);
                        startActivity(intent);

                    }
                }
            }
    );


}