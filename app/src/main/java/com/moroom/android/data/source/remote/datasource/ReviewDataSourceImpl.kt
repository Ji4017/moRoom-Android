package com.moroom.android.data.source.remote.datasource

import androidx.compose.runtime.key
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import com.moroom.android.data.source.remote.model.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewDataSourceImpl @Inject constructor(
    private val database: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth,
    private val crashlytics: FirebaseCrashlytics
) : ReviewDataSource {
    override suspend fun saveReviewToFirebase(review: Review): Unit =
        withContext(Dispatchers.IO) {
            try {
                database.getReference("Address")
                    .child(review.address)
                    .push()
                    .setValue(review)
            } catch (error: Exception) {
                crashlytics.setUserId(firebaseAuth.uid!!)
                crashlytics.setCustomKey("title", review.title)
                crashlytics.setCustomKey("checkedItems", review.checkedItems)
                crashlytics.setCustomKey("pros", review.pros)
                crashlytics.setCustomKey("cons", review.cons)
                crashlytics.setCustomKey("createdAt", review.createdAt)
                crashlytics.recordException(error)
            }
        }
}