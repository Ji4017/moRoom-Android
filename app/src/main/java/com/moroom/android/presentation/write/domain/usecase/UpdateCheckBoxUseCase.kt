package com.moroom.android.presentation.write.domain.usecase

import com.moroom.android.presentation.write.domain.repository.CheckItemRepository
import javax.inject.Inject

class UpdateCheckBoxUseCase @Inject constructor(private val repository: CheckItemRepository) {
    suspend operator fun invoke(text: String, isChecked: Boolean) {
        repository.updateCheckItem(text, isChecked)
    }
}