package com.moroom.android.data.repository

import com.moroom.android.data.source.remote.mapper.DataMappers
import com.moroom.android.data.source.remote.datasource.ReviewDataSource
import com.moroom.android.domain.model.WrittenReview
import com.moroom.android.domain.repository.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewDataSource: ReviewDataSource,
    private val dataMappers: DataMappers
) : ReviewRepository {
    override suspend fun saveReviewToFireBase(writtenReview: WrittenReview) {
        val reviewModel = dataMappers.mapToDataModel(writtenReview)
        reviewDataSource.saveReviewToFirebase(reviewModel)
    }
}