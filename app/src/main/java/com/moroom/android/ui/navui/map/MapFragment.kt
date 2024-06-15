package com.moroom.android.ui.navui.map

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.moroom.android.R
import com.moroom.android.databinding.FragmentMapBinding
import com.moroom.android.ui.login.MoveToLogin
import com.moroom.android.ui.result.ResultActivity
import com.moroom.android.ui.search.SearchActivity
import com.moroom.android.ui.write.WriteActivity
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPOIItem.CalloutBalloonButtonType
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.MapView.CurrentLocationEventListener
import net.daum.mf.map.api.MapView.POIItemEventListener

class MapFragment : Fragment(), CurrentLocationEventListener, MapView.MapViewEventListener, POIItemEventListener {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private var mapView: MapView? = null
    private var mapViewContainer: ViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMapView()
        setUpListener()
    }

    private fun showMapView() {
        initializeMapView()
        configureMapView()
        setMapCenterAndZoom()
        loadLocationDataFromFirebase()
    }

    private fun initializeMapView() {
        mapView = MapView(requireContext())
        mapViewContainer = binding.mapView
        (mapViewContainer as RelativeLayout).addView(mapView)
    }

    private fun configureMapView() {
        mapView!!.setMapViewEventListener(this)
        mapView!!.setCalloutBalloonAdapter(CustomCalloutBalloonAdapter())
        mapView!!.setPOIItemEventListener(this)
    }

    private fun setMapCenterAndZoom() {
        val chungjuUniversityPoint = MapPoint.mapPointWithGeoCoord(CHUNGJU_UNIVERSITY_LATITUDE, CHUNGJU_UNIVERSITY_LONGITUDE)
        mapView!!.setMapCenterPoint(chungjuUniversityPoint, true)
        mapView!!.setZoomLevel(2, true)
    }

    private fun loadLocationDataFromFirebase() {
        var latitude: Double?
        var longitude: Double?
        var address: String?

        val databaseRef = FirebaseDatabase.getInstance().getReference("Address")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (addressSnapshot in dataSnapshot.children) {
                    address = addressSnapshot.key
                    latitude = addressSnapshot.child("latitude").getValue(Double::class.java)
                    longitude = addressSnapshot.child("longitude").getValue(Double::class.java)
                    if (latitude != null && longitude != null && address != null) {
                        addMarkerToMap(latitude!!, longitude!!, address!!)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(this::class.java.simpleName, databaseError.message)
            }
        })
    }

    private fun addMarkerToMap(latitude: Double, longitude: Double, address: String) {
        val marker = MapPOIItem()
        marker.itemName = address
        marker.mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)
        marker.markerType = MapPOIItem.MarkerType.CustomImage
        marker.customImageResourceId = R.drawable.red_pin
        marker.isCustomImageAutoscale = false
        marker.setCustomImageAnchor(0.5f, 1.0f)
        mapView!!.addPOIItem(marker)
    }

    internal inner class CustomCalloutBalloonAdapter : CalloutBalloonAdapter {
        private val mCalloutBalloon: View = layoutInflater.inflate(R.layout.custom_callout_ballon, mapViewContainer, false)

        override fun getCalloutBalloon(poiItem: MapPOIItem): View {
            // 마커 클릭 시 나오는 말풍선
            // Log.d("getCalloutBallon", "마커 클릭");

            // 각 마커의 주소 가져오기
            val address = poiItem.itemName
            (mCalloutBalloon.findViewById<View>(R.id.balloon_address) as TextView).text = address
            return mCalloutBalloon
        }

        override fun getPressedCalloutBalloon(poiItem: MapPOIItem): View {
            // 말풍선 클릭 시
            // Log.d("getPressedCalloutBallon", "말풍선 클릭");
            return mCalloutBalloon
        }
    }

    private fun setUpListener() {
        binding.etAddress.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            getSearchResult.launch(intent)
        }

        binding.fab.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val intent = if (user != null) {
                Intent(requireContext(), WriteActivity::class.java)
            } else {
                Intent(requireContext(), MoveToLogin::class.java)
            }
            startActivity(intent)
        }
    }

    private val getSearchResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null) {
                val data = result.data!!.getStringExtra("data")
                val intent = Intent(requireContext(), ResultActivity::class.java)
                intent.putExtra("searchedAddress", data)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove the MapView from the container to prevent memory leaks
        if (mapViewContainer != null) {
            mapViewContainer!!.removeView(mapView)
        }
    }


    // Implement other necessary methods for MapView.CurrentLocationEventListener and MapView.MapViewEventListener

    override fun onMapViewInitialized(mapView: MapView) {
    }

    override fun onCurrentLocationUpdate(mapView: MapView, mapPoint: MapPoint, v: Float) {
    }

    override fun onCurrentLocationDeviceHeadingUpdate(mapView: MapView, v: Float) {
    }

    override fun onCurrentLocationUpdateFailed(mapView: MapView) {
    }

    override fun onCurrentLocationUpdateCancelled(mapView: MapView) {
    }

    override fun onMapViewCenterPointMoved(mapView: MapView, mapPoint: MapPoint) {
    }

    override fun onMapViewZoomLevelChanged(mapView: MapView, i: Int) {
    }

    override fun onMapViewSingleTapped(mapView: MapView, mapPoint: MapPoint) {
    }

    override fun onMapViewDoubleTapped(mapView: MapView, mapPoint: MapPoint) {
    }

    override fun onMapViewLongPressed(mapView: MapView, mapPoint: MapPoint) {
    }

    override fun onMapViewDragStarted(mapView: MapView, mapPoint: MapPoint) {
    }

    override fun onMapViewDragEnded(mapView: MapView, mapPoint: MapPoint) {
    }

    override fun onMapViewMoveFinished(mapView: MapView, mapPoint: MapPoint) {
    }

    override fun onPOIItemSelected(mapView: MapView, mapPOIItem: MapPOIItem) {
    }

    @Deprecated("Deprecated in Java")
    override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView, mapPOIItem: MapPOIItem) {
    }

    override fun onCalloutBalloonOfPOIItemTouched(
        mapView: MapView,
        mapPOIItem: MapPOIItem,
        calloutBalloonButtonType: CalloutBalloonButtonType
    ) {
        // 말풍선 클릭 시
        // Log.d("onCalloutBalloonOfPOIItemTouched", "POIItem 터치 됨");
        val intent = Intent(requireContext(), ResultActivity::class.java)
        intent.putExtra("searchedAddress", mapPOIItem.itemName)
        startActivity(intent)
    }

    override fun onDraggablePOIItemMoved(
        mapView: MapView,
        mapPOIItem: MapPOIItem,
        mapPoint: MapPoint
    ) {
        // 마커의 속성 중 isDraggable = true 일 때 마커를 이동 시켰을 경우
    }


    companion object {
        private const val CHUNGJU_UNIVERSITY_LATITUDE = 36.6522355
        private const val CHUNGJU_UNIVERSITY_LONGITUDE = 127.4946216
    }
}
