package com.moroom.android.data.repository

import com.moroom.android.data.source.remote.mapper.DataMappers
import com.moroom.android.data.source.remote.datasource.ReviewDataSource
import com.moroom.android.domain.model.WrittenReview
import com.moroom.android.domain.repository.ReviewRepository
import com.moroom.android.presentation.result.ReviewState
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewDataSource: ReviewDataSource,
    private val dataMappers: DataMappers
) : ReviewRepository {
    override val reviewsState: StateFlow<ReviewState> = reviewDataSource.reviewsState

    override suspend fun fetchReview(address: String) {
        reviewDataSource.fetchReview(address)
    }

    override suspend fun saveReview(writtenReview: WrittenReview) {
        val reviewModel = dataMappers.mapToDataModel(writtenReview)
        reviewDataSource.saveReview(reviewModel)
    }
}