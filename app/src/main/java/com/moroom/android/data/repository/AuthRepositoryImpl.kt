package com.moroom.android.data.repository

import com.moroom.android.data.source.remote.datasource.AuthDataSource
import com.moroom.android.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authDataSource: AuthDataSource) :
    AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> =
        authDataSource.login(email, password)

    override suspend fun signUp(email: String, password: String): Result<Unit> =
        authDataSource.signUp(email, password)
}