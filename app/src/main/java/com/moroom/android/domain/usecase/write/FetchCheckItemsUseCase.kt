package com.moroom.android.domain.usecase.write

import com.moroom.android.domain.repository.CheckItemRepository
import javax.inject.Inject

class FetchCheckItemsUseCase @Inject constructor(private val repository: CheckItemRepository) {
    suspend operator fun invoke() {
        repository.fetchCheckItemsFromFirebase()
    }
}