package com.moroom.android.data.firebase;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moroom.android.data.model.WrittenReviewData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveReviewWithLatLng {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final FirebaseAuth auth = FirebaseAuth.getInstance();
    final FirebaseUser firebaseUser = auth.getCurrentUser();
//    private double latitue;
//    private double latitue;

    // 주소를 이용하여 위도와 경도를 가져오는 메서드
    public void getLatLngFromAddress(String address, Context context) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);

            if (addressList != null && !addressList.isEmpty()) {
                double latitude = addressList.get(0).getLatitude();
                double longitude = addressList.get(0).getLongitude();

                // 위도와 경도를 Firebase에 저장하는 메서드 호출
                saveLatLngToDB(address, latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLatLngToDB(String address, double latitude, double longitude) {
        // 위도 경도 DB에 저장
        
        DatabaseReference addressRef = database.getReference("Address");
        Query query = addressRef.orderByKey().equalTo(address);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Firebase", "dataSnapshot : " + dataSnapshot);
                for (DataSnapshot addressSnapshot : dataSnapshot.getChildren()) {
                    if (addressSnapshot.hasChild("latitude") && addressSnapshot.hasChild("longitude")) {
                        Log.d("Firebase", "Address already has latitude and longitude.");
                        return;
                    }
                }
                // 위도와 경도가 저장되어 있지 않은 경우 저장
                DatabaseReference newAddressRef = addressRef.child(address);
                newAddressRef.child("latitude").setValue(latitude);
                newAddressRef.child("longitude").setValue(longitude);
                Log.d("Firebase", "latitude, longitude saved for: " + address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "latitude, longitude DatabaseError: " + databaseError.getMessage());
            }
        });
    }


    public void saveReviewToDB(String address, String title, ArrayList<String> checkedTextList ,String goodThingMultiLine, String badThingMultiLine){

        if (!address.isEmpty()){

            WrittenReviewData writtenReviewData = new WrittenReviewData();
            writtenReviewData.setIdToken(firebaseUser.getUid());
            writtenReviewData.setAddress(address);
            writtenReviewData.setTitle(title);
            writtenReviewData.setGoodThing(goodThingMultiLine);
            writtenReviewData.setBadThing(badThingMultiLine);

            DatabaseReference databaseRef = database.getReference("Address").child(address).push();
            databaseRef.setValue(writtenReviewData);

            // 사용자가 체크한 리스트의 텍스트값만 담겨진 리스트를 for each 문으로 DB에 저장
            for (String checkedText : checkedTextList) {
                writtenReviewData.setSelectedListText(checkedText);
                databaseRef.child("selectedList").push().setValue(checkedText);
            }

            // 리뷰를 작성했으니 UserAccount의 review 노드의 값을 true로 바꿔줌
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("UserAccount");

            Map<String, Object> update = new HashMap<>();
            update.put(user.getUid() + "/review", true);

            // 저장된 데이터 업데이트
            usersRef.updateChildren(update);

        }
    }
}
