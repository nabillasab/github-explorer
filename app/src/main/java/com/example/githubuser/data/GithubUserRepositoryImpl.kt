package com.example.githubuser.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.githubuser.data.local.repo.RepositoryDao
import com.example.githubuser.data.local.user.UserDao
import com.example.githubuser.data.mediator.GithubRepoRemoteMediator
import com.example.githubuser.data.mediator.GithubSearchRemoteMediator
import com.example.githubuser.data.mediator.GithubUserRemoteMediator
import com.example.githubuser.data.network.GithubDataSource
import com.example.githubuser.di.IoDispatcher
import com.example.githubuser.domain.GithubUserRepository
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.userdetail.model.Repository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@Singleton
class GithubUserRepositoryImpl @Inject constructor(
    private val networkDataSource: GithubDataSource,
    private val userRemoteMediator: GithubUserRemoteMediator,
    private val searchRemoteMediator: GithubSearchRemoteMediator,
    private val repoRemoteMediator: GithubRepoRemoteMediator,
    private val userDao: UserDao,
    private val repositoryDao: RepositoryDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GithubUserRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getUserList(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 10,
                initialLoadSize = 20
            ),
            remoteMediator = userRemoteMediator,
            pagingSourceFactory = { userDao.getUsersResult() }
        ).flow.map { pagingData ->
            pagingData.map { it.toModel() }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getUserByUsername(query: String): Flow<PagingData<User>> {
        searchRemoteMediator.setQueryParam(query)
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            remoteMediator = searchRemoteMediator,
            pagingSourceFactory = { userDao.getSearchResult(query) }
        ).flow.map { pagingData ->
            pagingData.map { it.toModel() }
        }
    }

    override fun getUserDetail(username: String): Flow<Result<User>> {
        return flow {
            emit(Result.Loading)
            val userDataCompleted = userDao.getUserDataCompleted(username)
            try {
                val userFreshExist = userDao.getFreshUserByUsername(username)

                if (userFreshExist != null || isDataStale(
                        userDataCompleted?.lastUpdated ?: 0L
                    )
                ) {
                    val response = networkDataSource.getUserDetail(username)
                    val user = response.toModel()
                    userDao.insert(response.toEntity())
                    emit(Result.Success(user))
                } else {
                    if (userDataCompleted != null) {
                        emit(Result.Success(userDataCompleted.toModel()))
                    }
                }
            } catch (exception: Exception) {
                emit(Result.Error(exception.message ?: "unexpected error"))
                if (userDataCompleted != null) {
                    emit(Result.Success(userDataCompleted.toModel()))
                }
            }
        }.flowOn(dispatcher)
    }

    private fun isDataStale(lastUpdated: Long): Boolean {
        return (System.currentTimeMillis() - lastUpdated > 20 * 60 * 1000)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getRepoList(username: String): Flow<PagingData<Repository>> {
        repoRemoteMediator.setUsername(username)
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 5,
                initialLoadSize = 20
            ),
            remoteMediator = repoRemoteMediator,
            pagingSourceFactory = {
                repositoryDao.getRepoByUserName(username)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toModel() }
        }
    }
}
