package com.moroom.android.ui.write.domain.usecase

import com.moroom.android.ui.write.domain.repository.CheckItemRepository
import javax.inject.Inject

class UpdateCheckBoxUseCase @Inject constructor(private val repository: CheckItemRepository) {
    suspend operator fun invoke(text: String, isChecked: Boolean) {
        repository.updateCheckItem(text, isChecked)
    }
}