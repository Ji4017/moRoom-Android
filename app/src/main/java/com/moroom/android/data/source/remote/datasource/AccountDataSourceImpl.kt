package com.moroom.android.data.source.remote.datasource

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.moroom.android.data.source.remote.model.UserAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume

class AccountDataSourceImpl @Inject constructor(
    private val database: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth
): AccountDataSource {
    override suspend fun updateUserReviewStatus(): Unit =
        withContext(Dispatchers.IO) {
            val usersRef = database.getReference("UserAccount")
            val reviewStatusData = mapOf("${firebaseAuth.uid}/review" to true)
            usersRef.updateChildren(reviewStatusData)
        }

    override suspend fun createUserInAuthentication(email: String, password: String): Int {
        return suspendCancellableCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(0)
                    } else {
                        if (task.exception is FirebaseAuthException) {
                            continuation.resume(1)
                        } else {
                            continuation.resume(2)
                        }
                    }
                }
        }
    }

    override suspend fun createUserInDB(userAccount: UserAccount): Int {
        val databaseReference = FirebaseDatabase.getInstance().getReference("UserAccount")

        return suspendCancellableCoroutine { continuation ->
            databaseReference.child(userAccount.idToken)
                .setValue(userAccount) { databaseError: DatabaseError?, _: DatabaseReference? ->
                    if (databaseError != null) {
                        Log.d(ContentValues.TAG, "유저 정보 DB 저장 실패: " + databaseError.message)
                        continuation.resume(3) // Inquiry code
                    } else {
                        Log.d(ContentValues.TAG, "유저 정보 DB 저장 성공")
                        continuation.resume(0) // Success code
                    }
                }
        }
    }
}