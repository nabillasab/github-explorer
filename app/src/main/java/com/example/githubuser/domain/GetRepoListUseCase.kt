package com.example.githubuser.domain

import androidx.paging.PagingData
import com.example.githubuser.ui.userdetail.model.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRepoListUseCase @Inject constructor(
    private val githubUserRepository: GithubUserRepository
) {
    fun execute(username: String): Flow<PagingData<Repository>> {
        return githubUserRepository.getRepoList(username)
    }
}