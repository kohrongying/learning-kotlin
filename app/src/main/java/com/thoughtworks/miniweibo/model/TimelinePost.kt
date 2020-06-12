package com.thoughtworks.miniweibo.model

data class TimelinePost(
    val id: String,
    val created_at: String,
    val text: String,
    val reposts_count: Int,
    val comments_count: Int,
    val attitudes_count: Int,
    val User: User
)