package com.example.capstone.navui.home;


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

import com.example.capstone.ContentsAdapter;
import com.example.capstone.R;
import com.example.capstone.Contents;
import com.example.capstone.SearchActivity;
import com.example.capstone.SearchedActivity;
import com.example.capstone.WriteActivity;
import com.example.capstone.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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


        ImageView mainImageView = binding.mainImage;
        EditText searchView = binding.etAddress;

        RecyclerView recyclerView = view.findViewById(R.id.homeRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = binding.fab;

        LinearLayout linearUam = binding.linearUam;
        LinearLayout linearYeJi = binding.linearYeJi;
        LinearLayout linearGukJe = binding.linearGukJe;
        LinearLayout linearJinWon = binding.linearJinWon;

        linearUam.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), SearchedActivity.class);
            intent.putExtra("searchedAddress", "충북 청주시 청원구 안덕벌로19번길 116 (내덕동) 우암마을");
            startActivity(intent);
        });

        linearYeJi.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), SearchedActivity.class);
            intent.putExtra("searchedAddress", "충북 청주시 청원구 안덕벌로19번길 116 (내덕동) 예지관");
            startActivity(intent);
        });

        linearGukJe.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), SearchedActivity.class);
            intent.putExtra("searchedAddress", "충북 청주시 청원구 안덕벌로19번길 116 (내덕동) 국제학사");
            startActivity(intent);
        });

        linearJinWon.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), SearchedActivity.class);
            intent.putExtra("searchedAddress", "충북 청주시 청원구 수암로66번길 48-2 (우암동, 한진 신세대 아파트)");
            startActivity(intent);
        });


        mainImageView.setClipToOutline(true);

        searchView.setFocusable(false);
        searchView.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            getSearchResult.launch(intent);
        });


        fab.setOnClickListener(view12 -> {
            Intent intent = new Intent(getActivity(), WriteActivity.class);
            startActivity(intent);

        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Address").child("메인용 후기");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Contents contents = dataSnapshot.getValue(Contents.class);
                    arrayList.add(contents);

                    // Log.d("HomeFragment", "DB data : " + dataSnapshot.getValue());
                    // Firebase DB에 있는 데이터 로그로 출력
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeFragment", String.valueOf(error.toException()));
            }
        });

        Contents contents = new Contents();
        contents.setBlur(false);

        Context context = getActivity();
        adapter = new ContentsAdapter(arrayList, context);
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // setResult에 의해 SearchActivity 로부터의 결과 값이 이곳으로 전달됨.
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        String data = result.getData().getStringExtra("data");

                        Intent intent = new Intent(getActivity(), SearchedActivity.class);
                        intent.putExtra("searchedAddress", data);
                        startActivity(intent);

                    }
                }
            }
    );
}