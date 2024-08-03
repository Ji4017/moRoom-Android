package com.moroom.android.presentation.write.domain.usecase

import com.moroom.android.presentation.write.domain.repository.CoordinatesRepository
import javax.inject.Inject

class CheckLatLngExistsUseCase @Inject constructor(private val coordinatesRepository: CoordinatesRepository) {
    suspend operator fun invoke(address: String) = coordinatesRepository.hasLatLngInFirebase(address)
}