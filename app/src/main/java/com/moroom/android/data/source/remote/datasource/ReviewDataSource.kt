package com.moroom.android.data.source.remote.datasource

import com.moroom.android.data.source.remote.model.Review
import com.moroom.android.presentation.result.ReviewState
import kotlinx.coroutines.flow.Flow

interface ReviewDataSource {
    val reviewsState: Flow<ReviewState>
    suspend fun fetchReview(address: String)
    suspend fun saveReview(review: Review)
}