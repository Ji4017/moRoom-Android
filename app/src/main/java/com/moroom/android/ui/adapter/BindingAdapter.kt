package com.moroom.android.ui.adapter

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moroom.android.data.model.Review
import com.moroom.android.ui.adapter.home.BestReviewAdapter

object ReviewBindingAdapter {
    @JvmStatic
    @BindingAdapter("bestReviewItems", "progressBar")
    fun setBestReviews(
        recyclerview: RecyclerView,
        bestReviews: ArrayList<Review>?,
        progressBar: ProgressBar
    ) {
        bestReviews?.let {
            progressBar.visibility = View.GONE
            recyclerview.adapter = BestReviewAdapter(bestReviews)
        }
    }
}