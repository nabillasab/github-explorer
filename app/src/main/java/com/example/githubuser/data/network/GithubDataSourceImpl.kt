package com.example.githubuser.data.network

import com.example.githubuser.BuildConfig
import com.example.githubuser.data.GithubRepositoryData
import com.example.githubuser.data.GithubUserData
import javax.inject.Inject

class GithubDataSourceImpl @Inject constructor(
    val githubApi: GithubApi
): GithubDataSource  {

    private fun getDefaultHeader(): MutableMap<String, String> {
        val headers = mutableMapOf<String, String>()
        headers["accept"] = "application/vnd.github+json"
        headers["X-GitHub-Api-Version"] = "2022-11-28"
        return headers
    }

    override suspend fun getUserList(): List<GithubUserData> {
        return githubApi.getUserList(getDefaultHeader())
    }

    override suspend fun getUserDetail(username: String): GithubUserData {
        return githubApi.getUserDetail(username, getDefaultHeader())
    }

    override suspend fun getRepoList(username: String): List<GithubRepositoryData> {
        val headers = getDefaultHeader()
        headers["Proxy-Authorization"] = "Bearer ${BuildConfig.API_KEY}"
        return githubApi.getRepositoryList(username, headers)
    }

    override suspend fun getUserByUsername(searchKey: String): GithubUserData {
        val queryMap = mutableMapOf<String, String>()
        queryMap["q"] = searchKey
        return githubApi.searchUser(queryMap, getDefaultHeader())
    }

}