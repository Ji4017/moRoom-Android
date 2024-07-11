package com.moroom.android.ui.write.domain.usecase

import com.moroom.android.ui.write.domain.repository.CheckItemRepository
import javax.inject.Inject

class FetchCheckItemsUseCase @Inject constructor(private val repository: CheckItemRepository) {
    suspend operator fun invoke() {
        repository.fetchCheckItemsFromFirebase()
    }
}