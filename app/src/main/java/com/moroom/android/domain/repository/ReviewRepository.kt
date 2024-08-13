package com.moroom.android.domain.repository

import com.moroom.android.domain.model.WrittenReview
import com.moroom.android.presentation.result.ReviewState
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    val reviewsState: Flow<ReviewState>
    suspend fun fetchReview(address: String)
    suspend fun saveReview(writtenReview: WrittenReview)
}