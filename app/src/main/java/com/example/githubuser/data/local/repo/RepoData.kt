package com.example.githubuser.data.local.repo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "github_repo")
data class RepositoryEntity(
    @PrimaryKey val id: Int,
    val userName: String,
    val repoName: String,
    val isPrivateRepo: Boolean,
    val repoUrl: String,
    val desc: String?,
    val isForkRepo: Boolean,
    val updatedAt: String,
    val starCount: Int,
    val language: String?,
    val forksCount: Int,
    val licenseName: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity
data class RemoteKeyRepoEntity(
    @PrimaryKey val username: String,
    val nextPage: Int?,
    val lastFetched: Long
)
