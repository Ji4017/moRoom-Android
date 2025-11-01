package com.moroom.android.presentation.nav.map

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapLogger
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.moroom.android.R
import com.moroom.android.databinding.FragmentMapBinding
import com.moroom.android.presentation.login.MoveToLogin
import com.moroom.android.presentation.result.ResultActivity
import com.moroom.android.presentation.search.SearchActivity
import com.moroom.android.presentation.write.WriteActivity


class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by viewModels()

    private lateinit var labelLayer: LabelLayer
    private lateinit var kakaoMap: KakaoMap

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
        setUpMapView()
        setUpListener()
    }

    private fun setUpMapView() {
        binding.mapView.start(lifeCycleCallback, readyCallback)
    }

    private val lifeCycleCallback: MapLifeCycleCallback = object : MapLifeCycleCallback() {
        override fun onMapDestroy() {
        }

        override fun onMapError(error: Exception) {
            MapLogger.e(error.message)
        }
    }

    private val readyCallback: KakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(map: KakaoMap) {
            kakaoMap = map
            labelLayer = kakaoMap.labelManager!!.layer!!
            createLabelsFromLocationData()
            setOnLabelClickListener(kakaoMap)
        }

        override fun getPosition(): LatLng {
            return LatLng.from(CHUNGJU_UNIVERSITY_LATITUDE, CHUNGJU_UNIVERSITY_LONGITUDE)
        }

        override fun isVisible(): Boolean {
            return true
        }
    }

    private fun setOnLabelClickListener(kakaoMap: KakaoMap) {
        kakaoMap.setOnLabelClickListener { _, _, label ->
            when {
                label.labelId.contains("parent") -> {
                    onParentLabelClicked(label)
                    true
                }
                label.labelId.contains("callout") -> {
                    onBalloonLabelClicked(label)
                    true
                }
                else -> false
            }
        }
    }

    private fun createLabelsFromLocationData() {
        viewModel.coordinates.observe(viewLifecycleOwner) { locationData ->
            locationData.forEachIndexed { index, it ->
                createLabels(index, it.address, it.latitude, it.longitude)
            }
        }
    }

    private fun createLabels(labelId: Int, address: String, latitude: Double, longitude: Double) {
        // Parent 라벨 추가
        val label = labelLayer.addLabel(
            LabelOptions.from("parent$labelId", LatLng.from(latitude, longitude))
                .setStyles(LabelStyle.from(R.drawable.red_pin).setAnchorPoint(0.5f, 0.5f))
        )
        label.scaleTo(2.5f, 2.5f)

        // CalloutBalloon 라벨 추가
        val balloonLabel: Label =
            createCalloutBalloonLabel("callout$labelId", address, latitude, longitude)
        label.addShareTransform(balloonLabel)
    }

    private fun createCalloutBalloonLabel(
        labelId: String,
        address: String,
        latitude: Double,
        longitude: Double
    ): Label {
        val viewGroup = LayoutInflater.from(requireContext())
            .inflate(R.layout.custom_callout_ballon, null) as ViewGroup

        val tv = viewGroup.findViewById<View>(R.id.tv_address) as TextView
        tv.text = address

        val bitmap = ViewToBitmap.createBitmap(viewGroup)

        val calloutLabel = labelLayer.addLabel(
            LabelOptions.from(labelId, LatLng.from(latitude, longitude))
                .setStyles(LabelStyle.from(bitmap))
                .setVisible(false)
        )
        calloutLabel.changePixelOffset(0f, -90f)
        calloutLabel.tag = address

        return calloutLabel
    }

    private fun onParentLabelClicked(label: Label) {
        val calloutBalloonLabelId = "callout" + label.labelId.substringAfter("parent")
        val calloutBalloonLabel = labelLayer.getLabel(calloutBalloonLabelId)
        calloutBalloonLabel?.let {
            if (it.isShow) {
                it.hide()
            } else {
                it.show()
                val cameraUpdate = CameraUpdateFactory.newCenterPosition(label.position)
                kakaoMap.moveCamera(cameraUpdate, CameraAnimation.from(200))
            }
        }
    }

    private fun onBalloonLabelClicked(label: Label) {
        val address = label.tag as? String
        if (!address.isNullOrEmpty()) {
            val intent = Intent(requireContext(), ResultActivity::class.java)
            intent.putExtra("searchedAddress", address)
            startActivity(intent)
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

    override fun onResume() {
        super.onResume()
        binding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.removeAllViews()
        _binding = null

    }

    companion object {
        private const val CHUNGJU_UNIVERSITY_LATITUDE = 36.6522355
        private const val CHUNGJU_UNIVERSITY_LONGITUDE = 127.4946216
    }
}
