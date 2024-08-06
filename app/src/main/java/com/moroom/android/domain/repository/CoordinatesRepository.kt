package com.moroom.android.domain.repository

import com.moroom.android.data.source.remote.model.Coordinates


interface CoordinatesRepository {
    suspend fun hasLatLngInFirebase(address: String): Boolean
    suspend fun getLatLngFromAddress(address: String): Pair<Double, Double>
    suspend fun saveLatLngToFirebase(coordinates: Coordinates)
}