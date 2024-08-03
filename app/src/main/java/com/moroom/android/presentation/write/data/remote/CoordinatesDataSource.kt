package com.moroom.android.presentation.write.data.remote

import com.moroom.android.presentation.write.data.model.Coordinates

interface CoordinatesDataSource {
    suspend fun hasLatLngInFirebase(address: String): Boolean
    suspend fun saveLatLngToFirebase(coordinates: Coordinates)
}