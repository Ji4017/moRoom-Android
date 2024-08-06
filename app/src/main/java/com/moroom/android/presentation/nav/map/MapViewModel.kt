package com.moroom.android.presentation.nav.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.moroom.android.data.source.remote.model.Coordinates

class MapViewModel : ViewModel() {
    private val _coordinates = MutableLiveData<ArrayList<Coordinates>>()
    val coordinates: LiveData<ArrayList<Coordinates>> = _coordinates

    init {
        loadLocationDataFromFirebase()
    }

    private fun loadLocationDataFromFirebase() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Address")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val coordinates = ArrayList<Coordinates>()
                for (addressSnapshot in dataSnapshot.children) {
                    val address = addressSnapshot.key
                    val latitude = addressSnapshot.child("latitude").getValue(Double::class.java)
                    val longitude = addressSnapshot.child("longitude").getValue(Double::class.java)
                    if (latitude != null && longitude != null && address != null) {
                        coordinates.add(Coordinates(address, latitude, longitude))
                    }
                }
                _coordinates.value = coordinates
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(this::class.java.simpleName, databaseError.message)
            }
        })
    }
}