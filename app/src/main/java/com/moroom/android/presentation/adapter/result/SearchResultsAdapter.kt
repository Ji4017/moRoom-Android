package com.moroom.android.presentation.adapter.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moroom.android.data.source.remote.model.Review
import com.moroom.android.databinding.ListItemBinding

class SearchResultsAdapter(private val reviewList: List<Review>) :
    RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(reviewList[position])
    }

    override fun getItemCount() = reviewList.size

    class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review) {
            binding.item = item
        }
    }
}
