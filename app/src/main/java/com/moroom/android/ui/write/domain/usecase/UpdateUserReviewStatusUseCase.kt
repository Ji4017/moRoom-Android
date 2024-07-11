package com.moroom.android.ui.write.domain.usecase

import com.moroom.android.ui.write.domain.repository.AccountRepository
import javax.inject.Inject

class UpdateUserReviewStatusUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend operator fun invoke() = accountRepository.updateUserReviewStatus()
}