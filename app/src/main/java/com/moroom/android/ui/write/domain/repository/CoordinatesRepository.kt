package com.moroom.android.ui.write.domain.repository

import com.moroom.android.ui.write.data.model.Coordinates


interface CoordinatesRepository {
    suspend fun hasLatLngInFirebase(address: String): Boolean
    suspend fun getLatLngFromAddress(address: String): Pair<Double, Double>
    suspend fun saveLatLngToFirebase(coordinates: Coordinates)
}