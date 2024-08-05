package com.moroom.android.data.source.remote.datasource

import com.moroom.android.data.source.remote.model.Review

interface ReviewDataSource {
    suspend fun saveReviewToFirebase(review: Review)
}