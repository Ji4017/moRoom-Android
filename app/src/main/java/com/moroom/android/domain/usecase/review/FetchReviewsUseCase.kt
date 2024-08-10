package com.moroom.android.domain.usecase.review

import com.moroom.android.domain.repository.ReviewRepository
import javax.inject.Inject

class FetchReviewsUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(address: String) = reviewRepository.fetchReview(address)
}