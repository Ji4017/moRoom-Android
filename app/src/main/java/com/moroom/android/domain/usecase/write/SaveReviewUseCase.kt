package com.moroom.android.domain.usecase.write

import com.moroom.android.domain.model.WrittenReview
import com.moroom.android.domain.repository.ReviewRepository
import javax.inject.Inject

class SaveReviewUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(writtenReview: WrittenReview) {
        reviewRepository.saveReview(writtenReview)
    }
}