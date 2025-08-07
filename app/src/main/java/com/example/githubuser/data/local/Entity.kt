package com.example.githubuser.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "github_user")
data class UserEntity(
    @PrimaryKey val userName: String,
    val id: Int,
    val avatarUrl: String?,
    val fullName: String?,
    val totalRepo: Int,
    val followers: Int,
    val following: Int,
    val bio: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)

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
data class RemoteKeyUserEntity(
    @PrimaryKey val id: Int,
    val nextSince: Int,
    val lastFetched: Long
)

@Entity
data class RemoteKeyRepoEntity(
    @PrimaryKey val id: Int,
    val username: String,
    val nextPage: Int,
    val lastFetched: Long
)