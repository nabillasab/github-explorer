package com.example.githubuser.domain

import com.example.githubuser.data.Result
import com.example.githubuser.ui.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserListUseCase @Inject constructor(
    private val githubUserRepository: GithubUserRepository
) {
    fun execute(): Flow<Result<List<User>>> {
        return githubUserRepository.getUserList()
    }
}