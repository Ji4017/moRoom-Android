package com.moroom.android.ui.write.data.remote

import com.google.firebase.database.FirebaseDatabase
import com.moroom.android.ui.write.data.model.ReviewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewDataSourceImpl @Inject constructor(
    private val database: FirebaseDatabase,
) : ReviewDataSource {
    override suspend fun saveReviewToFirebase(reviewModel: ReviewModel): Unit =
        withContext(Dispatchers.IO) {
            database.getReference("Address")
                .child(reviewModel.address)
                .push()
                .setValue(reviewModel)
        }
}