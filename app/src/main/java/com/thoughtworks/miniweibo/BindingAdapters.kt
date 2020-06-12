package com.thoughtworks.miniweibo

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thoughtworks.miniweibo.model.TimelinePost
import com.thoughtworks.miniweibo.ui.home.PostGridAdapter

@BindingAdapter("listData")
fun bindRecylcerView(recyclerView: RecyclerView, data: List<TimelinePost>?) {
    val adapter = recyclerView.adapter as PostGridAdapter
    adapter.submitList(data)
}