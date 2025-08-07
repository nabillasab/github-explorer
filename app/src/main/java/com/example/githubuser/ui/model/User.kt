package com.example.githubuser.ui.model

data class User(
    val id: Int,
    val username: String,
    val avatarUrl: String?,
    val fullName: String?,
    val repoCount: Int,
    val followers: Int,
    val following: Int,
    val bio: String?
)