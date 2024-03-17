package com.example.moroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.moroom.databinding.ActivityMyReviewBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyReviewActivity extends AppCompatActivity {

    private ActivityMyReviewBinding binding;
    private RecyclerView.Adapter adapter;
    final ArrayList<Contents> arrayList = new ArrayList<>();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        View blankView = findViewById(R.id.blank_view);
//        TextView tvDelete = findViewById(R.id.tv_delete);
        RecyclerView recyclerView = binding.myReviewRecyclerView;
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        blankView.setVisibility(View.VISIBLE);
//        tvDelete.setVisibility(View.VISIBLE);

        CheckReviewExistence();
        ShowMyReview();

    }

    // 이거를 Boolean으로 해서 if(CheckReviewExistence)로 해야되나?
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


    private void ShowMyReview() {
        // 디비에 있는 리뷰를 보여줌
        // child에 user.uid를 넣어야 되나?
        DatabaseReference databaseReference = database.getReference("Address").child(user.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                if (snapshot.exists()) {
                    // 유저의 후기가 있을 때
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getKey().equals("latitude") || dataSnapshot.getKey().equals("longitude")) {
                            // 필요없는 "latitude", "longitude", 등등등등등 값은 건너뜀
                            continue;
                        }

                        Contents contents = dataSnapshot.getValue(Contents.class);
                        arrayList.add(contents);


                        // DB에 데이터 잘 받아 오는지 로그로 출력
                        // Log.d("SearchedActivity", "DB data : " + dataSnapshot.getValue());
                    }
                    adapter.notifyDataSetChanged();

                } else {
                    // 해당 주소의 후기가 없을 때
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SearchedActivity", String.valueOf(error.toException()));
            }
        });
    }
}