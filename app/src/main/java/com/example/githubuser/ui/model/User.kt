package com.example.githubuser.ui.model

import com.example.githubuser.ui.userdetail.model.Repository

data class User(
    val username: String,
    val avatarUrl: String?,
    val fullName: String?,
    val repoCount: Int,
    val followers: Int,
    val following: Int,
    val bio: String?
)

data class UserDetail(
    val user: User,
    val repos: List<Repository>
)