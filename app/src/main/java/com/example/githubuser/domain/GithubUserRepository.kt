package com.example.githubuser.domain

import androidx.paging.PagingData
import com.example.githubuser.data.Result
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.userdetail.model.Repository
import kotlinx.coroutines.flow.Flow

interface GithubUserRepository {

    fun getUserList(): Flow<PagingData<User>>

    fun getUserDetail(username: String): Flow<Result<User>>

    fun getRepoList(username: String): Flow<PagingData<Repository>>

    fun getUserByUsername(query: String): Flow<PagingData<User>>
}