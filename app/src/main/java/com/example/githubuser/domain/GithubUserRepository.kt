package com.example.githubuser.domain

import com.example.githubuser.data.Result
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.userdetail.model.Repository
import kotlinx.coroutines.flow.Flow

interface GithubUserRepository {

    fun getUserList(): Flow<Result<List<User>>>

    fun getUserDetail(username: String): Flow<Result<User>>

    fun getRepoList(username: String): Flow<Result<List<Repository>>>

    fun getUserByUsername(username: String): Flow<Result<User>>
}