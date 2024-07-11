package com.moroom.android.ui.write.domain.usecase

import com.moroom.android.ui.write.domain.model.WrittenReview
import com.moroom.android.ui.write.domain.repository.ReviewRepository
import javax.inject.Inject

class SaveReviewUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(writtenReview: WrittenReview) {
        reviewRepository.saveReviewToFireBase(writtenReview)
    }
}