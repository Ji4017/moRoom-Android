package com.moroom.android.presentation.write.data.repository

import com.moroom.android.presentation.write.data.model.mapper.DataMappers
import com.moroom.android.presentation.write.data.remote.ReviewDataSource
import com.moroom.android.presentation.write.domain.model.WrittenReview
import com.moroom.android.presentation.write.domain.repository.ReviewRepository
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