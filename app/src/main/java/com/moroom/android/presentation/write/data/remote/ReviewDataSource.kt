package com.moroom.android.presentation.write.data.remote

import com.moroom.android.presentation.write.data.model.ReviewModel

interface ReviewDataSource {
    suspend fun saveReviewToFirebase(reviewModel: ReviewModel)
}