package com.moroom.android.ui.write.data.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import com.moroom.android.ui.write.data.model.Coordinates
import com.moroom.android.ui.write.data.remote.CoordinatesDataSource
import com.moroom.android.ui.write.domain.repository.CoordinatesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class CoordinatesRepositoryImpl @Inject constructor(
    private val coordinatesDataSource: CoordinatesDataSource,
    @ApplicationContext private val context: Context,
) : CoordinatesRepository {
    override suspend fun hasLatLngInFirebase(address: String): Boolean = coordinatesDataSource.hasLatLngInFirebase(address)

    override suspend fun getLatLngFromAddress(address: String): Pair<Double, Double> {
        var latitude = 0.0
        var longitude = 0.0
        val geocoder = Geocoder(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocationName(address, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: List<Address>) {
                    if (addresses.isNotEmpty()) {
                        latitude = addresses[0].latitude
                        longitude = addresses[0].longitude
                    }
                }
                override fun onError(errorMessage: String?) {
                    Log.e("Geocoding", "Error: $errorMessage")
                }
            })
        } else {
            try {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocationName(address, 1)
                if (!addresses.isNullOrEmpty()) {
                    latitude = addresses[0].latitude
                    longitude = addresses[0].longitude
                }
            } catch (e: IOException) {
                Log.e("Geocoding", "Error: ${e.message}")
            }
        }
        return Pair(longitude, latitude)
    }

    override suspend fun saveLatLngToFirebase(coordinates: Coordinates) {
        coordinatesDataSource.saveLatLngToFirebase(coordinates)
    }
}