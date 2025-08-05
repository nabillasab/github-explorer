package com.example.githubuser.data.network

import com.example.githubuser.BuildConfig
import com.example.githubuser.data.GithubRepositoryData
import com.example.githubuser.data.GithubUserData
import javax.inject.Inject

class GithubDataSourceImpl @Inject constructor(
    val githubApi: GithubApi
) : GithubDataSource {

    override suspend fun getUserDetail(username: String): GithubUserData {
        return githubApi.getUserDetail(username, AuthHelper.getDefaultHeader())
    }

    override suspend fun getRepoList(username: String): List<GithubRepositoryData> {
        val headers = AuthHelper.getDefaultHeader()
        headers["Proxy-Authorization"] = "Bearer ${BuildConfig.API_KEY}"
        return githubApi.getRepositoryList(username, headers)
    }
}