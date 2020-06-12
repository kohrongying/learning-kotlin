package com.thoughtworks.miniweibo.model

data class TimelinePost(
    val id: String,
    val created_at: String,
    val text: String,
    val reposts_count: String,
    val comments_count: String,
    val attitudes_count: String,
    val user: User
)