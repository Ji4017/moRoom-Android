package com.moroom.android.ui.write.domain.repository

import com.moroom.android.ui.write.domain.model.WrittenReview

interface ReviewRepository {
    suspend fun saveReviewToFireBase(writtenReview: WrittenReview)
}