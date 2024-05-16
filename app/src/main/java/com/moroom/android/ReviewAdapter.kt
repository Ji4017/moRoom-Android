package com.moroom.android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moroom.android.databinding.ListItemBinding

class ReviewAdapter(private val reviewList: ArrayList<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val item = reviewList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = reviewList.size

    class ReviewViewHolder private constructor(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review) {
            binding.tvTitle.text = item.title
            binding.tvGoodThing.text = item.goodThing
            binding.tvBadThing.text = item.badThing

            item.selectedList?.let { selectedList ->
                val selectedList = selectedList.values.joinToString(separator = "\n")
                binding.tvSelectedList.text = selectedList
            } ?: run {
                binding.tvSelectedList.text = ""
            }
        }

        companion object {
            fun from(parent: ViewGroup): ReviewViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBinding.inflate(layoutInflater, parent, false)
                return ReviewViewHolder(binding)
            }
        }
    }
}