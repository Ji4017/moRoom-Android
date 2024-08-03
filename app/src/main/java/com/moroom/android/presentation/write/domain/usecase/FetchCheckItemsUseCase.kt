package com.moroom.android.presentation.write.domain.usecase

import com.moroom.android.presentation.write.domain.repository.CheckItemRepository
import javax.inject.Inject

class FetchCheckItemsUseCase @Inject constructor(private val repository: CheckItemRepository) {
    suspend operator fun invoke() {
        repository.fetchCheckItemsFromFirebase()
    }
}