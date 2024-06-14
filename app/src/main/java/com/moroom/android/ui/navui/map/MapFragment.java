package com.moroom.android.ui.navui.map;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.moroom.android.ui.login.MoveToLogin;
import com.moroom.android.R;
import com.moroom.android.ui.result.ResultActivity;
import com.moroom.android.ui.search.SearchActivity;
import com.moroom.android.ui.write.WriteActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MapFragment extends Fragment implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.POIItemEventListener {
    private static final double CHUNGJU_UNIV_LATITUDE = 36.6522355;
    private static final double CHUNGJU_UNIV_LONGITUDE = 127.4946216;
    private MapView mapView;
    private ViewGroup mapViewContainer;

    private MapPOIItem marker;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private String address;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        EditText searchView = view.findViewById(R.id.et_address);
        FloatingActionButton fab = view.findViewById(R.id.fab);

        searchView.setFocusable(false);

        searchView.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            getSearchResult.launch(intent);
        });

        fab.setOnClickListener(view12 -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Intent intent;
            if (user != null) {
                intent = new Intent(getActivity(), WriteActivity.class);
            } else {
                intent = new Intent(getActivity(), MoveToLogin.class);
            }

            startActivity(intent);
        });

        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                // Log.d("키해시는 :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // 권한ID 가져옴
        int permission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET);
        int permission2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        int permission3 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);

        // 권한이 열려있는지 확인
        if (permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED || permission3 == PackageManager.PERMISSION_DENIED) {
            // 권한 체크(INTERNET, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1000);
        }


        // mapView 초기화 및 설정
        mapView = new MapView(getActivity());
        mapViewContainer = view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);


        // mapView 구성하기
        mapView.setMapViewEventListener(this);
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        mapView.setPOIItemEventListener(this);


        // 지도의 중심 및 줌 레벨 설정
        MapPoint chungjuUnivPoint = MapPoint.mapPointWithGeoCoord(CHUNGJU_UNIV_LATITUDE, CHUNGJU_UNIV_LONGITUDE);
        mapView.setMapCenterPoint(chungjuUnivPoint, true);
        mapView.setZoomLevel(2, true);

        // DB에서 경도 위도와 주소 데이터 가져오기
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Address");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot addressSnapshot : dataSnapshot.getChildren()) {
                    latitude = addressSnapshot.child("latitude").getValue(Double.class);
                    longitude = addressSnapshot.child("longitude").getValue(Double.class);
                    address = addressSnapshot.getKey();
                    // Log.d("Data", "Latitude: " + latitude);
                    // Log.d("Data", "Longitude: " + longitude);
                    // Log.d("Data", "주소: " + address);

                    if (latitude != null && longitude != null && address != null) {

                        // Step 3: 지도에 마커 추가하기
                        MapPoint markerPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);

                        marker = new MapPOIItem();
                        marker.setItemName(address);
                        marker.setMapPoint(markerPoint);
                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                        marker.setCustomImageResourceId(R.drawable.red_pin);
                        marker.setCustomImageAutoscale(false);
                        marker.setCustomImageAnchor(0.5f, 1.0f);

                        // marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);

                        mapView.addPOIItem(marker);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 처리 중 오류 발생 시 처리할 내용
            }
        });

        // createCustomMarker(mapView);

        // mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        return view;
    }


    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_ballon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            // 마커 클릭 시 나오는 말풍선
            // Log.d("getCalloutBallon", "마커 클릭 됨");
            String address = poiItem.getItemName();  // 각 마커의 주소 가져오기
            ((TextView) mCalloutBalloon.findViewById(R.id.balloon_address)).setText(address);
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            // 말풍선 클릭 시
            // Log.d("getPressedCalloutBallon", "말풍선 클릭 됨");
            return mCalloutBalloon;
        }
    }

    // 권한 체크 이후 로직
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            // 권한 체크에 동의하지 않으면 앱 종료
            if (!check_result) {
                requireActivity().finish();
            } else {
                // 권한이 허용된 경우, 지도 뷰 새로고침
                mapViewContainer.removeAllViews();
                mapViewContainer.addView(mapView);
            }
        }
    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // setResult에 의해 SearchActivity 로부터의 결과 값이 이곳으로 전달됨.
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        String data = result.getData().getStringExtra("data");

                        Intent intent = new Intent(getActivity(), ResultActivity.class);
                        intent.putExtra("searchedAddress", data);
                        startActivity(intent);

                    }
                }
            }
    );

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove the MapView from the container to prevent memory leaks
        if (mapViewContainer != null) {
            mapViewContainer.removeView(mapView);
        }
    }


    // Implement other necessary methods for MapView.CurrentLocationEventListener and MapView.MapViewEventListener
    // ...

    @Override
    public void onMapViewInitialized(MapView mapView) {
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {
    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        // 말풍선 클릭 시
        // Log.d("onCalloutBalloonOfPOIItemTouched", "POIItem 터치 됨");
        Intent intent = new Intent(getActivity(), ResultActivity.class);
        intent.putExtra("searchedAddress", mapPOIItem.getItemName());
        startActivity(intent);
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
        // 마커의 속성 중 isDraggable = true 일 때 마커를 이동 시켰을 경우
    }


}
