package com.moroom.android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moroom.android.data.model.Review
import com.moroom.android.databinding.ListItemBinding

class BestReviewAdapter(private val reviewList: ArrayList<Review>) :
    RecyclerView.Adapter<BestReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }

    override fun getItemCount(): Int = reviewList.size
}