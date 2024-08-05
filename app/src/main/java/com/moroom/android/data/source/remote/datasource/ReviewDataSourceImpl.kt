package com.moroom.android.data.source.remote.datasource

import com.google.firebase.database.FirebaseDatabase
import com.moroom.android.data.source.remote.model.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewDataSourceImpl @Inject constructor(
    private val database: FirebaseDatabase,
) : ReviewDataSource {
    override suspend fun saveReviewToFirebase(review: Review): Unit =
        withContext(Dispatchers.IO) {
            database.getReference("Address")
                .child(review.address)
                .push()
                .setValue(review)
        }
}