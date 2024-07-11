package com.moroom.android.ui.write.data.repository

import com.moroom.android.ui.write.data.remote.AccountDataSource
import com.moroom.android.ui.write.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(private val accountDataSource: AccountDataSource) :
    AccountRepository {
    override suspend fun updateUserReviewStatus() = accountDataSource.updateUserReviewStatus()
}