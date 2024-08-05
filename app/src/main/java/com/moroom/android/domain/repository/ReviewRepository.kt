package com.moroom.android.domain.repository

import com.moroom.android.domain.model.WrittenReview

interface ReviewRepository {
    suspend fun saveReviewToFireBase(writtenReview: WrittenReview)
}