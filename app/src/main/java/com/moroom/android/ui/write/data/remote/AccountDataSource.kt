package com.moroom.android.ui.write.data.remote

interface AccountDataSource {
    suspend fun updateUserReviewStatus()
}