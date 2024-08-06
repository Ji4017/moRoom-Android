package com.moroom.android.domain.usecase.write

import com.moroom.android.domain.repository.AccountRepository
import javax.inject.Inject

class UpdateUserReviewStatusUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend operator fun invoke() = accountRepository.updateUserReviewStatus()
}