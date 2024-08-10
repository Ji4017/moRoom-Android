package com.moroom.android.domain.usecase.review

import com.moroom.android.domain.repository.ReviewRepository
import javax.inject.Inject

class GetReviewsUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {
    operator fun invoke() = reviewRepository.reviewsState
}