package com.moroom.android.ui.adapter

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moroom.android.data.source.local.BestReview
import com.moroom.android.ui.adapter.home.BestReviewAdapter

object ReviewBindingAdapter {
    @JvmStatic
    @BindingAdapter("bestReviewItems", "progressBar")
    fun setBestReviews(
        recyclerview: RecyclerView,
        bestReviews: List<BestReview>?,
        progressBar: ProgressBar
    ) {
        bestReviews?.let {
            progressBar.visibility = View.GONE
            recyclerview.adapter = BestReviewAdapter(bestReviews)
        }
    }
}