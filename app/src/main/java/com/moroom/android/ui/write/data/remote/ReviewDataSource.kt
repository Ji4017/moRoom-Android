package com.moroom.android.ui.write.data.remote

import com.moroom.android.ui.write.data.model.ReviewModel

interface ReviewDataSource {
    suspend fun saveReviewToFirebase(reviewModel: ReviewModel)
}