package com.moroom.android.presentation.nav.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.moroom.android.data.source.local.BestReview
import com.moroom.android.data.repository.BestReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val bestReviewRepository: BestReviewRepository) : ViewModel() {
    private val _bestReviews = MutableLiveData<List<BestReview>>()
    val bestReviews: LiveData<List<BestReview>>
        get() = _bestReviews

    init {
        fetchBestReviews()
    }

    private fun fetchBestReviews() {
        viewModelScope.launch(Dispatchers.IO) {
            // 로딩 속도 개선을 위해 repository가 아닌 viewModel에서 스레드 변경
            val reviews = getCachedBestReviews()
            if(!reviews.isNullOrEmpty()) _bestReviews.postValue(reviews!!)
            else _bestReviews.postValue(getBestReviewsFromRemote())
        }
    }

    private fun getCachedBestReviews(): List<BestReview>? {
        return bestReviewRepository.getCachedBestReviews()

    }

    private suspend fun getBestReviewsFromRemote(): List<BestReview>? {
        return bestReviewRepository.loadAndCacheBestReviews()
    }
}