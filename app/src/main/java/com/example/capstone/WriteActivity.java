package com.example.capstone;


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
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.databinding.ActivityWriteBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WriteActivity extends AppCompatActivity {

    private ActivityWriteBinding binding;
    private RecyclerView checkListRecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CheckedTextViewData> arrayList;
    private DatabaseReference databaseReference;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final FirebaseAuth auth = FirebaseAuth.getInstance();
    final FirebaseUser firebaseUser = auth.getCurrentUser();
    public EditText etAddress;
    private EditText etGoodThingMultiLine;
    private EditText etBadThingMultiLine;
    private CheckedTextView checkedTextView;
    private String selectedFloor, selectedYear, untillTheYear , selectedRentType;

    private ArrayList<String> checkedTextList;  // 체크된 텍스트를 저장할 리스트
    private SaveReviewWithLatLng saveReviewWithLatLng = new SaveReviewWithLatLng();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        etAddress = binding.etAddress;
        Spinner floorSpinner = binding.spinner1;
        Spinner yearSpinner = binding.spinner2;
        Spinner rentTypeSpinner = binding.spinner3;
        TextView betweenTextView = binding.spinnerTextView;
        etGoodThingMultiLine = binding.goodMultiLine;
        etBadThingMultiLine = binding.badMultiLine;
        Button writeButton = binding.writeButton;
        untillTheYear = betweenTextView.getText().toString();
        checkedTextView = findViewById(R.id.checkedTextView);

        checkedTextList = new ArrayList<>();  // 체크된 텍스트를 저장할 리스트 초기화

        etAddress.setFocusable(false);
//        detailAddress.setFocusable(false);
        etAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WriteActivity.this, SearchActivity.class);
                getSearchResult.launch(intent);
            }
        });

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = etAddress.getText().toString();
                String title = selectedFloor + " " + selectedYear + untillTheYear + " " + selectedRentType;
                String goodThingMultiLine = etGoodThingMultiLine.getText().toString();
                String badThingMultiLine = etBadThingMultiLine.getText().toString();

                if (!etAddress.getText().toString().isEmpty()){
                    saveReviewWithLatLng.getLatLngFromAddress(address, WriteActivity.this);
                    saveReviewWithLatLng.saveReviewToDB(address, title, checkedTextList, goodThingMultiLine, badThingMultiLine );
                    Toast.makeText(WriteActivity.this, "후기작성 완료", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(WriteActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                else{
                    Toast.makeText(WriteActivity.this, "주소를 설정해주세요", Toast.LENGTH_LONG).show();
                }
            }
        });


        checkListRecyclerView = findViewById(R.id.checkListRecyclerView); // 아이디 연결
        checkListRecyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        checkListRecyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // CheckedTextViewData 객체를 담을 어레이 리스트 (어댑터쪽으로)


        // DB에 만들어놓은 리스트데이터 recyclerView에다가 가져오는 작업
        databaseReference = database.getReference("CheckedTextViewList"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CheckedTextViewData checkedTextViewData = dataSnapshot.getValue(CheckedTextViewData.class); // 만들어뒀던 CheckedTextViewData 객체에 데이터 담음.
                    arrayList.add(checkedTextViewData); // 담은 데이터들을 배열 리스트에 넣고 리사이클러뷰로 보낼 준비
                     Log.d("WriteActivity", "DB data : " + dataSnapshot.getValue());
                    // Firebase DB에 있는 데이터 잘 받아 왔는지 로그로 출력
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로 고침

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("WriteActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        adapter = new CheckedTextViewAdapter(arrayList, this, this);
        checkListRecyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결


        // 사용자가 선택한 Spinner 값 텍스트 추출
        floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedFloor = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedYear = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        rentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRentType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // setResult에 의해 SearchActivity 로부터의 결과 값이 이곳으로 전달된다.
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        String data = result.getData().getStringExtra("data");
                        etAddress.setText(data);
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