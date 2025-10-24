package com.example.githubuser.data.network

import com.example.githubuser.data.GithubUserData
import javax.inject.Inject

class GithubDataSourceImpl @Inject constructor(
    val githubApi: GithubApi
) : GithubDataSource {

    override suspend fun getUserDetail(username: String): GithubUserData {
        return githubApi.getUserDetail(username, AuthHelper.getDefaultHeader())
    }
}
