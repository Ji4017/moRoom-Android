package com.moroom.android.ui.write.data.repository

import com.moroom.android.ui.write.data.model.mapper.DataMappers
import com.moroom.android.ui.write.data.remote.ReviewDataSource
import com.moroom.android.ui.write.domain.model.WrittenReview
import com.moroom.android.ui.write.domain.repository.ReviewRepository
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