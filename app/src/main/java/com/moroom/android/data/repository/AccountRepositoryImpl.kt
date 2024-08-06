package com.moroom.android.data.repository

import com.moroom.android.data.source.remote.datasource.AccountDataSource
import com.moroom.android.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(private val accountDataSource: AccountDataSource) :
    AccountRepository {
    override suspend fun updateUserReviewStatus() = accountDataSource.updateUserReviewStatus()
}