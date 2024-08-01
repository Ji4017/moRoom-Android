package com.moroom.android.ui.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moroom.android.data.local.BestReview
import com.moroom.android.databinding.ListBestReviewItemBinding

class BestReviewAdapter(private val reviewList: List<BestReview>) :
    RecyclerView.Adapter<BestReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(val binding: ListBestReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BestReview) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ListBestReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }

    override fun getItemCount(): Int = reviewList.size
}