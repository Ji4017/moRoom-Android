package com.example.capstone;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.databinding.ActivitySearchedBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchedActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private final ArrayList<Contents> arrayList = new ArrayList<>();
    public String searchedAddress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.capstone.databinding.ActivitySearchedBinding binding = ActivitySearchedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerView = binding.searchedRecyclerView;
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ImageView blurView = binding.blurView;

        TextView addressTitle = binding.tvAddressTitle;
        TextView pleaseMessage1 = binding.pleaseMessage1;
        TextView pleaseMessage2 = binding.pleaseMessage2;
        TextView pleaseMessage3 = binding.pleaseMessage3;

        FloatingActionButton fab = binding.fab;
        Button moveToWrite = binding.moveToWrite;



        fab.setOnClickListener(view -> {
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

        // TextView에 사용자가 검색한 주소명 설정
        addressTitle.setText(searchedAddress);



        // 현재 유저가 작성한 리뷰가 있는지 없는지 확인하기 위함
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserAccount").child(uid);

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
                            Log.d("Review Value", "if문 진입 - " + reviewValue + " 이스 블러는 false로");
                            contents.setBlur(false);
                        }

                        // 사용자가 작성한 리뷰가 없는 경우 Blur값을 true로
                        else {
                            Log.d("Review Value", "else문 진입 - " + reviewValue + " 이스 블러는 트루로");
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


}