package com.moroom.android.presentation.write.domain.repository

import com.moroom.android.presentation.write.domain.model.WrittenReview

interface ReviewRepository {
    suspend fun saveReviewToFireBase(writtenReview: WrittenReview)
}