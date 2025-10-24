package com.example.githubuser.data.network

import com.example.githubuser.data.GithubUserData

interface GithubDataSource {

    suspend fun getUserDetail(username: String): GithubUserData
}
