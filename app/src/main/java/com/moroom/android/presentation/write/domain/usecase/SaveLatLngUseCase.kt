package com.moroom.android.presentation.write.domain.usecase

import com.moroom.android.presentation.write.data.model.Coordinates
import com.moroom.android.presentation.write.domain.repository.CoordinatesRepository
import javax.inject.Inject

class SaveLatLngUseCase @Inject constructor(private val coordinatesRepository: CoordinatesRepository) {
    suspend operator fun invoke(coordinates: Coordinates) = coordinatesRepository.saveLatLngToFirebase(coordinates)
}