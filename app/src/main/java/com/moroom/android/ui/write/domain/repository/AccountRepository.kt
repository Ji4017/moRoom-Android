package com.moroom.android.ui.write.domain.repository

interface AccountRepository {
    suspend fun updateUserReviewStatus()
}