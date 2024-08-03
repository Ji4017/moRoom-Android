package com.moroom.android.presentation.write.data.remote

interface AccountDataSource {
    suspend fun updateUserReviewStatus()
}