package com.moroom.android.domain.usecase.write

import com.moroom.android.domain.repository.CheckItemRepository
import javax.inject.Inject

class UpdateCheckBoxUseCase @Inject constructor(private val repository: CheckItemRepository) {
    suspend operator fun invoke(text: String, isChecked: Boolean) {
        repository.updateCheckItem(text, isChecked)
    }
}