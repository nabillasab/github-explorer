package com.example.githubuser.data.network

import com.example.githubuser.data.GithubRepositoryData
import com.example.githubuser.data.GithubSearchUserData
import com.example.githubuser.data.GithubUserData
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface GithubApi {

    @GET("/users")
    suspend fun getUserList(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int = 20,
        @HeaderMap headers: Map<String, String>
    ): List<GithubUserData>

    @GET("/users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String,
        @HeaderMap headers: Map<String, String>
    ): GithubUserData

    @GET("/users/{username}/repos")
    suspend fun getRepositoryList(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 20,
        @HeaderMap headers: Map<String, String>
    ): List<GithubRepositoryData>

    @GET("/search/users")
    suspend fun searchUser(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int = 20,
        @QueryMap queryParams: Map<String, String>,
        @HeaderMap headers: Map<String, String>
    ): GithubSearchUserData

}