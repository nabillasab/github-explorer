package com.example.githubuser.data.local.user

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
    var lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_source")
data class UserSourceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val sourceType: String,
    val searchQuery: String? = null,
    val position: Int,
    val timestamp: Long
)

@Entity(tableName = "user_remote_key")
data class RemoteKeyUserEntity(
    @PrimaryKey val id: String,
    val nextKey: Int?,
    val lastUpdate: Long
)

@Entity(tableName = "search_remote_key")
data class RemoteKeySearchEntity(
    @PrimaryKey val searchQuery: String,
    val nextPage: Int?,
    val lastUpdate: Long,
)