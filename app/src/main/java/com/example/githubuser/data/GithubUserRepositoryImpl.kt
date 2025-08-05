package com.example.githubuser.data

import com.example.githubuser.data.network.GithubDataSource
import com.example.githubuser.di.IoDispatcher
import com.example.githubuser.domain.GithubUserRepository
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.userdetail.model.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubUserRepositoryImpl @Inject constructor(
    private val networkDataSource: GithubDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GithubUserRepository {

    override fun getUserList(): Flow<Result<List<User>>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = networkDataSource.getUserList()
                val users = response.map { userMapper(it) }
                emit(Result.Success(users))
            } catch (exception: Exception) {
                emit(Result.Error(exception.message ?: "unexpected error"))
            }
        }.flowOn(dispatcher)
    }

    override fun getUserDetail(username: String): Flow<Result<User>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = networkDataSource.getUserDetail(username)
                val user = userMapper(response)
                emit(Result.Success(user))
            } catch (exception: Exception) {
                emit(Result.Error(exception.message ?: "unexpected error"))
            }
        }.flowOn(dispatcher)
    }

    override fun getRepoList(username: String): Flow<Result<List<Repository>>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = networkDataSource.getRepoList(username)
                val repos = response.map { repoMapper(it) }
                emit(Result.Success(repos))
            } catch (exception: Exception) {
                emit(Result.Error(exception.message ?: "unexpected error"))
            }
        }.flowOn(dispatcher)
    }

    override fun getUserByUsername(username: String): Flow<Result<User>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = networkDataSource.getUserByUsername(username)
                val user = userMapper(response)
                emit(Result.Success(user))
            } catch (exception: Exception) {
                emit(Result.Error(exception.message ?: "unexpected error"))
            }
        }.flowOn(dispatcher)
    }

    private fun userMapper(githubUser: GithubUserData): User {
        return User(
            username = githubUser.userName,
            avatarUrl = githubUser.avatarUrl,
            fullName = githubUser.fullName,
            repoCount = githubUser.totalRepo,
            followers = githubUser.followers,
            following = githubUser.following,
            bio = githubUser.bio
        )
    }

    private fun repoMapper(githubRepo: GithubRepositoryData): Repository {
        return Repository(
            name = githubRepo.repoName,
            description = githubRepo.desc,
            langRepo = githubRepo.language,
            star = githubRepo.starCount,
            repoUrl = githubRepo.repoUrl,
            fork = githubRepo.isForkRepo,
            updatedAt = githubRepo.updatedAt,
            private = githubRepo.isPrivateRepo,
            forksCount = githubRepo.forksCount,
            licenseName = githubRepo.license?.name
        )
    }
}