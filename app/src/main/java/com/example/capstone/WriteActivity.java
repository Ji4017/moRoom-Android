package com.example.capstone;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    public TextView address;
    private EditText textMultiLine;
    private CheckedTextView checkedTextView;
    private String title, selectedFloor, selectedYear, untillTheYear , selectedRentType;

    private ArrayList<String> checkedTextList;  // 체크된 텍스트를 저장할 리스트


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        address = binding.address;
        Spinner floorSpinner = binding.spinner1;
        Spinner yearSpinner = binding.spinner2;
        Spinner rentTypeSpinner = binding.spinner3;
        TextView betweenTextView = binding.spinnerTextView;
        textMultiLine = binding.textMultiLine;
        Button writeButton = binding.writeButton;
        untillTheYear = betweenTextView.getText().toString();
        checkedTextView = findViewById(R.id.checkedTextView);

        checkedTextList = new ArrayList<>();  // 체크된 텍스트를 저장할 리스트 초기화

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReviewToDB();
            }
        });

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



        checkListRecyclerView = findViewById(R.id.checkListRecyclerView); // 아디 연결
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

    }


    // 사용자가 작성한 리뷰 Firebase DB에 저장
    private void saveReviewToDB(){
        title = selectedFloor + " " + selectedYear + " " + untillTheYear + " " + selectedRentType;

        WrittenReviewData writtenReviewData = new WrittenReviewData();
        writtenReviewData.setIdToken(firebaseUser.getUid());
        writtenReviewData.setAddress(address.getText().toString());
        writtenReviewData.setTitle(title);
        writtenReviewData.setOpinion(textMultiLine.getText().toString());

        databaseReference = database.getReference("Address").child(address.getText().toString()).push();
        databaseReference.setValue(writtenReviewData);
        
        // 사용자가 체크한 리스트의 텍스트값만 담겨진 리스트를 for each 문으로 DB에 저장
        for (String checkedText : checkedTextList) {
            writtenReviewData.setSelectedListText(checkedText);
            databaseReference.child("selectedList").push().setValue(checkedText);
        }

        Toast.makeText(WriteActivity.this, "후기작성 완료", Toast.LENGTH_LONG).show();
    }

    // 체크된 텍스트를 리스트에 추가
    public void addTextToList(String text) {
        checkedTextList.add(text);
    }

    // 체크 해제된 텍스트를 리스트에서 제거
    public void removeTextFromList(String text) {
        checkedTextList.remove(text);
    }



}