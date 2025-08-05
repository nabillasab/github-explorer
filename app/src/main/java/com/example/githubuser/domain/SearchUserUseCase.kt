package com.example.githubuser.domain

import androidx.paging.PagingData
import com.example.githubuser.ui.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUserUseCase @Inject constructor(
    private val githubUserRepository: GithubUserRepository
) {
    fun execute(query: String): Flow<PagingData<User>> {
        return githubUserRepository.getUserByUsername(query)
    }
}