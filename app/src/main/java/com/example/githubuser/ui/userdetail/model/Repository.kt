package com.example.githubuser.ui.userdetail.model

data class Repository(
    val id: Int,
    val name: String,
    val description: String?,
    val langRepo: String?,
    val star: Int,
    val repoUrl: String,
    val fork: Boolean,
    val updatedAt: String,
    val private: Boolean,
    val forksCount: Int,
    val licenseName: String?
)