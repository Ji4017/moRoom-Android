package com.moroom.android.domain.repository

interface AccountRepository {
    suspend fun updateUserReviewStatus()
    suspend fun createUserInAuthentication(email: String, password: String): Int
    suspend fun createUserInDB(email: String, password: String): Int
}