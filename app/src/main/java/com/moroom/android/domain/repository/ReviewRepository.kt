package com.moroom.android.domain.repository

import com.moroom.android.domain.model.WrittenReview
import com.moroom.android.presentation.result.ReviewState
import kotlinx.coroutines.flow.StateFlow

interface ReviewRepository {
    val reviewsState: StateFlow<ReviewState>
    suspend fun fetchReview(address: String)
    suspend fun saveReview(writtenReview: WrittenReview)
}