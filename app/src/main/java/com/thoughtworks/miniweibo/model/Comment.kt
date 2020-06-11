package com.thoughtworks.miniweibo.model

import com.thoughtworks.miniweibo.model.User

data class Comment (
    val id: String,
    val created_at: String,
    val text: String,
    val user: User
)