package com.example.githubuser.domain

import com.example.githubuser.data.Result
import com.example.githubuser.ui.userdetail.model.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRepoListUseCase @Inject constructor(
    private val githubUserRepository: GithubUserRepository
) {
    fun execute(username: String): Flow<Result<List<Repository>>> {
        return githubUserRepository.getRepoList(username)
    }
}