package com.example.githubuser.data

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

data class GithubUserData(
    @SerializedName("login")
    val userName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("name")
    val fullName: String?,
    @SerializedName("public_repos")
    val totalRepo: Int,
    @SerializedName("followers")
    val followers: Int,
    @SerializedName("following")
    val following: Int,
    @SerializedName("bio")
    val bio: String?,
)

data class GithubRepositoryData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val repoName: String,
    @SerializedName("private")
    val isPrivateRepo: Boolean,
    @SerializedName("html_url")
    val repoUrl: String,
    @SerializedName("description")
    val desc: String?,
    @SerializedName("fork")
    val isForkRepo: Boolean,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("stargazers_count")
    val starCount: Int,
    @SerializedName("language")
    val language: String?,
    @SerializedName("forks_count")
    val forksCount: Int,
    @SerializedName("license")
    val license: GithubLisence?
)

data class GithubLisence(
    @SerializedName("name")
    val name: String?
)

data class GithubSearchUserData(
    @SerializedName("items")
    val users: List<GithubUserData>,
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean
)

data class RemoteKeyUser(
    @PrimaryKey val id: Int,
    val prevKey: Int?,
    val nextKey: Int?,
    val queryType: String,
    val query: String? = null,
    val nextSince: Int,
    val lastFetched: Long
)