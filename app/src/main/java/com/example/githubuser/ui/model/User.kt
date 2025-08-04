package com.example.githubuser.ui.model

data class User(
    val username: String,
    val avatarUrl: String?,
    val fullName: String?,
    val repositories: Int,
    val followers: Int,
    val following: Int,
    val bio: String?
)