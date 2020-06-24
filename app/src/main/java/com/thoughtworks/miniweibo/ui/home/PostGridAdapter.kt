package com.thoughtworks.miniweibo.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thoughtworks.miniweibo.databinding.GridPostItemBinding
import com.thoughtworks.miniweibo.model.TimelinePost

class PostGridAdapter(val clickListener: PostListener) : ListAdapter<TimelinePost, PostGridAdapter.TimelinePostViewHolder>(DiffCallback){
    class TimelinePostViewHolder(private var binding: GridPostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: TimelinePost, clickListener: PostListener) {
            binding.post = post
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TimelinePost>() {
        override fun areItemsTheSame(oldItem: TimelinePost, newItem: TimelinePost): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TimelinePost, newItem: TimelinePost): Boolean {
            return oldItem.id == newItem.id
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostGridAdapter.TimelinePostViewHolder {
        return TimelinePostViewHolder((GridPostItemBinding.inflate(LayoutInflater.from(parent.context))))
    }

    override fun onBindViewHolder(holder: PostGridAdapter.TimelinePostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post, clickListener)
    }
}

class PostListener(val clickListener: (id: String) -> Unit) {
    fun onClick(post: TimelinePost) = clickListener(post.id)
}