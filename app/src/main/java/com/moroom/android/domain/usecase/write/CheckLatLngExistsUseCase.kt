package com.moroom.android.domain.usecase.write

import com.moroom.android.domain.repository.CoordinatesRepository
import javax.inject.Inject

class CheckLatLngExistsUseCase @Inject constructor(private val coordinatesRepository: CoordinatesRepository) {
    suspend operator fun invoke(address: String) = coordinatesRepository.hasLatLngInFirebase(address)
}