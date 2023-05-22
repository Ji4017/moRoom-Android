package com.example.capstone.ui.home;


import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


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
import com.google.android.material.snackbar.Snackbar;
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

        RecyclerView recyclerView = view.findViewById(R.id.homeRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        EditText searchView = binding.etAddress;
        FloatingActionButton fab = binding.fab;

        searchView.setFocusable(false);

        searchView.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            getSearchResult.launch(intent);
        });


        fab.setOnClickListener(view12 -> {
//            Snackbar.make(view12, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
            Intent intent = new Intent(getActivity(), WriteActivity.class);
            startActivity(intent);

        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Address").child("검색한 주소명 타이틀로 들어감");
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

        adapter = new ContentsAdapter(arrayList);
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