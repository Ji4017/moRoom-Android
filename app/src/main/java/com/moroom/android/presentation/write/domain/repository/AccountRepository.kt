package com.moroom.android.presentation.write.domain.repository

interface AccountRepository {
    suspend fun updateUserReviewStatus()
}