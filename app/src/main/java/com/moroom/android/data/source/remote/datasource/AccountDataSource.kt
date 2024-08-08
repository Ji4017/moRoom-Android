package com.moroom.android.data.source.remote.datasource

import com.moroom.android.data.source.remote.model.UserAccount

interface AccountDataSource {
    suspend fun updateUserReviewStatus()
    suspend fun createUserInAuthentication(email: String, password: String): Int
    suspend fun createUserInDB(userAccount: UserAccount): Int
}