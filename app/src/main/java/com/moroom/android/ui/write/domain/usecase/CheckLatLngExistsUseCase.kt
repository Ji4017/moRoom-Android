package com.moroom.android.ui.write.domain.usecase

import com.moroom.android.ui.write.domain.repository.CoordinatesRepository
import javax.inject.Inject

class CheckLatLngExistsUseCase @Inject constructor(private val coordinatesRepository: CoordinatesRepository) {
    suspend operator fun invoke(address: String) = coordinatesRepository.hasLatLngInFirebase(address)
}