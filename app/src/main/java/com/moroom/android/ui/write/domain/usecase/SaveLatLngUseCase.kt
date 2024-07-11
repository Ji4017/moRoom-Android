package com.moroom.android.ui.write.domain.usecase

import com.moroom.android.ui.write.data.model.Coordinates
import com.moroom.android.ui.write.domain.repository.CoordinatesRepository
import javax.inject.Inject

class SaveLatLngUseCase @Inject constructor(private val coordinatesRepository: CoordinatesRepository) {
    suspend operator fun invoke(coordinates: Coordinates) = coordinatesRepository.saveLatLngToFirebase(coordinates)
}