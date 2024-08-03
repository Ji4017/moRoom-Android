package com.moroom.android.presentation.nav.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.moroom.android.data.model.LocationData

class MapViewModel : ViewModel() {
    private val _locationData = MutableLiveData<ArrayList<LocationData>>()
    val locationData: LiveData<ArrayList<LocationData>> = _locationData

    init {
        loadLocationDataFromFirebase()
    }

    private fun loadLocationDataFromFirebase() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Address")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val locations = ArrayList<LocationData>()
                for (addressSnapshot in dataSnapshot.children) {
                    val address = addressSnapshot.key
                    val latitude = addressSnapshot.child("latitude").getValue(Double::class.java)
                    val longitude = addressSnapshot.child("longitude").getValue(Double::class.java)
                    if (latitude != null && longitude != null && address != null) {
                        locations.add(LocationData(address, latitude, longitude))
                    }
                }
                _locationData.value = locations
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(this::class.java.simpleName, databaseError.message)
            }
        })
    }
}