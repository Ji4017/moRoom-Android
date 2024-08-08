package com.moroom.android.data.source.remote.datasource

interface AuthDataSource {
    suspend fun login(email: String, password:String): Result<Unit>
    suspend fun signUp(email: String, password:String): Result<Unit>
}