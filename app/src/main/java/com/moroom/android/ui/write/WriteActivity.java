package com.moroom.android.ui.write;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.moroom.android.R;
import com.moroom.android.data.firebase.SaveReviewWithLatLng;
import com.moroom.android.data.model.CheckedTextViewData;
import com.moroom.android.databinding.ActivityWriteBinding;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moroom.android.ui.adapter.write.CheckedTextViewAdapter;
import com.moroom.android.ui.navui.MainActivity;
import com.moroom.android.ui.search.SearchActivity;

import java.util.ArrayList;
import java.util.function.Consumer;

public class WriteActivity extends AppCompatActivity {
    private ActivityWriteBinding binding;
    private RecyclerView.Adapter adapter;
    private String address;
    private String selectedFloor, selectedYear, selectedRentType;
    private ArrayList<CheckedTextViewData> arrayList = new ArrayList<>(); // CheckedTextViewData 객체 담을 배열 리스트
    private ArrayList<String> checkedTextList = new ArrayList<>();  // 체크된 항목을 저장할 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViews();
        setupListeners();

    }

    private void setupViews() {
        setupRecyclerView();
        setCheckList();
        setDefaultSpinnerOptions();
    }

    private void setupSpinnerItemSelectedListener(Spinner spinner, Consumer<String> selectionConsumer) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                // selected 값을 Consumer를 통해 처리
                selectionConsumer.accept(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void setupSpinnerListeners() {
        setupSpinnerItemSelectedListener(binding.spinnerFloor, selected -> selectedFloor = selected);
        setupSpinnerItemSelectedListener(binding.spinnerYear, selected -> selectedYear = selected);
        setupSpinnerItemSelectedListener(binding.spinnerRentType, selected -> selectedRentType = selected);
    }

    private void setDefaultSpinnerOptions() {
        ArrayAdapter<CharSequence> floorAdapter = ArrayAdapter.createFromResource(this, R.array.floor_array, android.R.layout.simple_spinner_item);
        binding.spinnerFloor.setAdapter(floorAdapter);
        binding.spinnerFloor.setSelection(1);

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this, R.array.year_array, android.R.layout.simple_spinner_item);
        binding.spinnerYear.setAdapter(yearAdapter);
        binding.spinnerYear.setSelection(yearAdapter.getCount() - 1);
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.checkListRecyclerView.setHasFixedSize(true);
        binding.checkListRecyclerView.setLayoutManager(layoutManager);
    }

    private void setupListeners() {
        setupSpinnerListeners();

        binding.etAddress.setFocusable(false);
        binding.etAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WriteActivity.this, SearchActivity.class);
                getSearchResult.launch(intent);
            }
        });

        binding.btWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()){
                    saveReview();
                    goToMainActivity();
                }
            }
        });
    }

    private boolean validateFields() {
        if (address == null) {
            Toast.makeText(WriteActivity.this, getString(R.string.please_enter_address), Toast.LENGTH_LONG).show();
            return false;
        }

        String pros = binding.etPros.getText().toString();
        if (pros.isEmpty()) {
            Toast.makeText(WriteActivity.this, getString(R.string.please_enter_pros), Toast.LENGTH_LONG).show();
            return false;
        }

        String cons = binding.etCons.getText().toString();
        if (cons.isEmpty()) {
            Toast.makeText(WriteActivity.this, getString(R.string.please_enter_cons), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void saveReview() {
        String yearSuffix = binding.tvYearSuffix.getText().toString();
        String title = selectedFloor + " " + selectedYear + yearSuffix + " " + selectedRentType;
        String pros = binding.etPros.getText().toString();
        String cons = binding.etCons.getText().toString();

        SaveReviewWithLatLng saveReviewWithLatLng = new SaveReviewWithLatLng();
        saveReviewWithLatLng.getLatLngFromAddress(address, WriteActivity.this);
        saveReviewWithLatLng.saveReviewToDB(address, title, checkedTextList, pros, cons);
        Toast.makeText(WriteActivity.this, getString(R.string.completed), Toast.LENGTH_LONG).show();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(WriteActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setCheckList() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("CheckedTextViewList");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CheckedTextViewData checkedTextViewData = dataSnapshot.getValue(CheckedTextViewData.class); // CheckedTextViewData 객체에 데이터 담음
                    arrayList.add(checkedTextViewData); // 담은 데이터를 리스트에 넣고 리사이클러뷰로 보낼 준비
                    // Log.d("WriteActivity", "DB data : " + dataSnapshot.getValue());
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로 고침
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("WriteActivity", String.valueOf(error.toException()));
            }
        });

        setupAdapter();
    }

    private void setupAdapter() {
        adapter = new CheckedTextViewAdapter(arrayList, this, this);
        binding.checkListRecyclerView.setAdapter(adapter);
    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // setResult에 의해 SearchActivity 로부터의 결과 값이 이곳으로 전달
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        String data = result.getData().getStringExtra("data");
                        address = data;
                        binding.etAddress.setText(data);
                    }
                }
            }
    );

    // 체크된 텍스트를 리스트에 추가
    public void addTextToList(String text) {
        checkedTextList.add(text);
    }

    // 체크 해제된 텍스트를 리스트에서 제거
    public void removeTextFromList(String text) {
        checkedTextList.remove(text);
    }
}