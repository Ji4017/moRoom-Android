package com.moroom.android.domain.repository

interface AccountRepository {
    suspend fun updateUserReviewStatus()
}