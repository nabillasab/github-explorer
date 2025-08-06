package com.example.githubuser.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.githubuser.data.network.GithubDataSource
import com.example.githubuser.data.network.GithubRepositoryPagingSource
import com.example.githubuser.data.network.GithubSearchUserPagingSource
import com.example.githubuser.data.network.GithubUserPagingSource
import com.example.githubuser.di.IoDispatcher
import com.example.githubuser.domain.GithubUserRepository
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.userdetail.model.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubUserRepositoryImpl @Inject constructor(
    private val networkDataSource: GithubDataSource,
    private val userListPagingSource: GithubUserPagingSource,
    private val searchUserPagingSource: GithubSearchUserPagingSource,
    private val repositoryPagingSource: GithubRepositoryPagingSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GithubUserRepository {

    override fun getUserList(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { userListPagingSource }).flow.map { pagingData ->
            pagingData.map { userMapper(it) }
        }
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

    override fun getRepoList(username: String): Flow<PagingData<Repository>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                repositoryPagingSource.apply {
                    setUserName(username)
                }
            }).flow.map { pagingData ->
            pagingData.map { repoMapper(it) }
        }
    }

    override fun getUserByUsername(query: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                searchUserPagingSource.apply {
                    setQueryText(query)
                }
            }).flow.map { pagingData ->
            pagingData.map { userMapper(it) }
        }
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