package com.moroom.android.data.source.remote.datasource

import com.moroom.android.data.source.remote.model.Coordinates

interface CoordinatesDataSource {
    suspend fun hasLatLngInFirebase(address: String): Boolean
    suspend fun saveLatLngToFirebase(coordinates: Coordinates)
}