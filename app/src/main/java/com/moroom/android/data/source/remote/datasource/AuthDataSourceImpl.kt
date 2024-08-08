package com.moroom.android.data.source.remote.datasource

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume

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

    override suspend fun sendEmail(email: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            firebaseAuth.sendSignInLinkToEmail(email, buildActionCodeSettings())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "이메일 보내기 성공.")
                        continuation.resume(true)
                    } else {
                        Log.e(TAG, "이메일 보내기 실패", task.exception)
                        continuation.resume(false)
                    }
                }
        }
    }

    private fun buildActionCodeSettings(): ActionCodeSettings {
        return ActionCodeSettings.newBuilder()
            .setUrl("https://moroom.page.link/m1r2")
            .setHandleCodeInApp(true)
            .setAndroidPackageName(
                "com.moroom.android", /* androidPackageName */
                false,  /* installIfNotAvailable */
                "12" /* minimumVersion */
            )
            .build()
    }
}