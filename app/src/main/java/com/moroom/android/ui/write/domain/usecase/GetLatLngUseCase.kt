package com.moroom.android.ui.write.domain.usecase

import com.moroom.android.ui.write.domain.repository.CoordinatesRepository
import javax.inject.Inject

class GetLatLngUseCase @Inject constructor(private val coordinatesRepository: CoordinatesRepository) {
    suspend operator fun invoke(address: String) = coordinatesRepository.getLatLngFromAddress(address)
}