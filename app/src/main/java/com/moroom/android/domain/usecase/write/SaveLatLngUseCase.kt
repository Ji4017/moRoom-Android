package com.moroom.android.domain.usecase.write

import com.moroom.android.data.source.remote.model.Coordinates
import com.moroom.android.domain.repository.CoordinatesRepository
import javax.inject.Inject

class SaveLatLngUseCase @Inject constructor(private val coordinatesRepository: CoordinatesRepository) {
    suspend operator fun invoke(coordinates: Coordinates) = coordinatesRepository.saveLatLngToFirebase(coordinates)
}