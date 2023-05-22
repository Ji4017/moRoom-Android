package com.example.capstone;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.databinding.ActivitySearchedBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

        TextView addressTitle = findViewById(R.id.tv_addressTitle);

        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Intent intent = new Intent(SearchedActivity.this, WriteActivity.class);
            startActivity(intent);
//                finish();

        });


        Intent intent = getIntent();
        searchedAddress = intent.getStringExtra("searchedAddress");

        addressTitle.setText(searchedAddress);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Address").child(searchedAddress);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals("latitude") || dataSnapshot.getKey().equals("longitude")) {
                        // "latitude" 또는 "longitude" 필드인 경우 건너뜁니다.
                        continue;
                    }

                    Contents contents = dataSnapshot.getValue(Contents.class);
                    arrayList.add(contents);

                    // Log.d("SearchedActivity", "DB data : " + dataSnapshot.getValue());
                    // Firebase DB에 있는 데이터 로그로 출력
                }
                adapter.notifyDataSetChanged();
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