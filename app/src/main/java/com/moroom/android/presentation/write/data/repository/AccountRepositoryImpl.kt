package com.moroom.android.presentation.write.data.repository

import com.moroom.android.presentation.write.data.remote.AccountDataSource
import com.moroom.android.presentation.write.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(private val accountDataSource: AccountDataSource) :
    AccountRepository {
    override suspend fun updateUserReviewStatus() = accountDataSource.updateUserReviewStatus()
}