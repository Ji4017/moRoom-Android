package com.moroom.android.presentation.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moroom.android.data.source.remote.model.Review
import com.moroom.android.domain.usecase.review.FetchReviewsUseCase
import com.moroom.android.domain.usecase.review.GetReviewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val getReviewsUseCase: GetReviewsUseCase,
    private val fetchReviewsUseCase: FetchReviewsUseCase
) : ViewModel() {

    val reviewsState: StateFlow<ReviewState> = getReviewsUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        ReviewState.Loading
    )

    fun fetchReviews(address: String) {
        viewModelScope.launch { fetchReviewsUseCase.invoke(address) }
    }
}

sealed class ReviewState {
    data object Loading : ReviewState()
    data class Success(val reviews: List<Review>) : ReviewState()
    data object Empty : ReviewState()
    data class Error(val message: String) : ReviewState()
}