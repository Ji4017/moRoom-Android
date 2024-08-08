package com.moroom.android.data.source.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    AuthDataSource {
    override suspend fun login(email: String, password: String): Result<Unit> =
        runCatching {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        }

    override suspend fun signUp(email: String, password: String): Result<Unit> =
        runCatching {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        }
}