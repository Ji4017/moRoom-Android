package com.moroom.android.domain.usecase.write

import com.moroom.android.domain.repository.CoordinatesRepository
import javax.inject.Inject

class GetLatLngUseCase @Inject constructor(private val coordinatesRepository: CoordinatesRepository) {
    suspend operator fun invoke(address: String) = coordinatesRepository.getLatLngFromAddress(address)
}