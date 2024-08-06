package com.moroom.android.data.source.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountDataSourceImpl @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
): AccountDataSource {
    override suspend fun updateUserReviewStatus(): Unit =
        withContext(Dispatchers.IO) {
            val usersRef = database.getReference("UserAccount")
            val reviewStatusData = mapOf("${auth.uid}/review" to true)
            usersRef.updateChildren(reviewStatusData)
        }
}