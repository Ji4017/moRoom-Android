package com.moroom.android.presentation.write.data.remote

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.moroom.android.presentation.write.data.model.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CoordinatesDataSourceImpl @Inject constructor(private val database: FirebaseDatabase) :
    CoordinatesDataSource {

    override suspend fun hasLatLngInFirebase(address: String): Boolean =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                database.getReference("Address").child(address).get()
                    .addOnSuccessListener { dataSnapshot ->
                        val result = dataSnapshot.hasChild("latitude") && dataSnapshot.hasChild("longitude")
                        continuation.resume(result)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("hasLatLngInFirebase()", exception.toString())
                        continuation.resume(false)
                    }
            }
        }

    override suspend fun saveLatLngToFirebase(coordinates: Coordinates): Unit =
        withContext(Dispatchers.IO) {
            val addressRef = database.getReference("Address").child(coordinates.address)
            addressRef.child("latitude").setValue(coordinates.latitude)
            addressRef.child("longitude").setValue(coordinates.longitude)
        }
}