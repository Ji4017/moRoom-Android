package com.moroom.android.data.source.remote.datasource

interface AccountDataSource {
    suspend fun updateUserReviewStatus()
}