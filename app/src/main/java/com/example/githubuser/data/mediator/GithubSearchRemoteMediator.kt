package com.example.githubuser.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.githubuser.data.local.user.RemoteKeySearchDao
import com.example.githubuser.data.local.user.RemoteKeySearchEntity
import com.example.githubuser.data.local.user.UserDao
import com.example.githubuser.data.local.user.UserEntity
import com.example.githubuser.data.local.user.UserSourceDao
import com.example.githubuser.data.local.user.UserSourceEntity
import com.example.githubuser.data.network.AuthHelper
import com.example.githubuser.data.network.GithubApi
import com.example.githubuser.data.toEntity
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class GithubSearchRemoteMediator @Inject constructor(
    private val transactionRunner: RoomTransactionRunner,
    private val userDao: UserDao,
    private val userSourceDao: UserSourceDao,
    private val remoteKeySearchDao: RemoteKeySearchDao,
    private val api: GithubApi,
) : RemoteMediator<Int, UserEntity>() {
    private var query: String? = null

    fun setQueryParam(query: String? = null) {
        this.query = query
    }

    //timeout caching
    override suspend fun initialize(): InitializeAction {
        val lastUpdated = remoteKeySearchDao.getRemoteKey(query ?: "")?.lastUpdate
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - (lastUpdated ?: 0)
        val cacheTime = 20 * 6 * 1000
        return if (diff <= cacheTime) { // 20 mins
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        val loadKey: Int = when (loadType) {
            LoadType.REFRESH -> {
                1
            }

            //Load next page (scrolling down)
            LoadType.APPEND -> {
                val remoteKey = remoteKeySearchDao.getRemoteKey(query ?: "")
                remoteKey?.nextPage ?: return MediatorResult.Success(true)
            }
            //Load previous page (scrolling up) — not always needed
            LoadType.PREPEND -> {
                return MediatorResult.Success(true)
            }
        }

        return try {
            val response = api.searchUser(
                page = loadKey,
                perPage = state.config.pageSize,
                query = query ?: "",
                headers = AuthHelper.getDefaultHeader()
            )
            val result = response.users.map { it.toEntity() }

            transactionRunner.runInTransaction {
                if (loadType == LoadType.REFRESH) {
                    userSourceDao.clearSearchSources(query ?: "")
                    remoteKeySearchDao.clearKey(query ?: "")

                    //clean up stale data when refreshing
                    val aWeekAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
                    userSourceDao.clearStaleData(aWeekAgo)
                }
                userDao.insertAll(result)

                val currCount = if (loadType == LoadType.REFRESH) {
                    0
                } else {
                    userSourceDao.getSourceCountCount(query ?: "")
                }

                val sources = result.mapIndexed { index, userEntity ->
                    UserSourceEntity(
                        id = userEntity.id,
                        userId = userEntity.userName,
                        sourceType = "SEARCH",
                        searchQuery = query ?: "",
                        position = currCount + index,
                        timestamp = userEntity.lastUpdated
                    )
                }
                userSourceDao.insertSources(sources)

                //update remote keys
                remoteKeySearchDao.insertOrReplace(
                    RemoteKeySearchEntity(
                        searchQuery = query ?: "",
                        nextPage = if (result.isEmpty()) null else loadKey + 1,
                        lastUpdate = System.currentTimeMillis()
                    )
                )
            }

            MediatorResult.Success(endOfPaginationReached = result.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}