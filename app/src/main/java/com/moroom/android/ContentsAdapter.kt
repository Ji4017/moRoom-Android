package com.moroom.android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moroom.android.databinding.ListItemBinding

class ContentsAdapter(private val arrayList: ArrayList<Contents>) :
    RecyclerView.Adapter<ContentsAdapter.ContentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentsViewHolder {
        return ContentsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ContentsViewHolder, position: Int) {
        val item = arrayList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = arrayList.size

    class ContentsViewHolder private constructor(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Contents) {
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
            fun from(parent: ViewGroup): ContentsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBinding.inflate(layoutInflater, parent, false)
                return ContentsViewHolder(binding)
            }
        }
    }
}