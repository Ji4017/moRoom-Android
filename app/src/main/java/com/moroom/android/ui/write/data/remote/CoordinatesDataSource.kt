package com.moroom.android.ui.write.data.remote

import com.moroom.android.ui.write.data.model.Coordinates

interface CoordinatesDataSource {
    suspend fun hasLatLngInFirebase(address: String): Boolean
    suspend fun saveLatLngToFirebase(coordinates: Coordinates)
}