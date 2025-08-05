package com.example.githubuser.data.network

import com.example.githubuser.data.GithubRepositoryData
import com.example.githubuser.data.GithubUserData

interface GithubDataSource {

    suspend fun getUserDetail(username: String): GithubUserData

    suspend fun getRepoList(username: String): List<GithubRepositoryData>
}