package com.example.githubuser.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.githubuser.data.local.repo.RemoteKeyRepoDao
import com.example.githubuser.data.local.repo.RemoteKeyRepoEntity
import com.example.githubuser.data.local.repo.RepositoryDao
import com.example.githubuser.data.local.repo.RepositoryEntity
import com.example.githubuser.data.network.AuthHelper
import com.example.githubuser.data.network.GithubApi
import com.example.githubuser.data.toEntity
import javax.inject.Inject

/**
 * Repository list pagination with Paging 3 and caching mechanism with Room
 */
@OptIn(ExperimentalPagingApi::class)
open class GithubRepoRemoteMediator @Inject constructor(
    private val transactionRunner: RoomTransactionRunner,
    private val repositoryDao: RepositoryDao,
    private val remoteKeyRepoDao: RemoteKeyRepoDao,
    private val api: GithubApi
) : RemoteMediator<Int, RepositoryEntity>() {

    private lateinit var username: String

    fun setUsername(username: String) {
        this.username = username
    }

    override suspend fun initialize(): InitializeAction {
        val lastUpdated = repositoryDao.getLastUpdated() ?: 0L
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - lastUpdated
        val cacheTime = 20 * 6 * 1000
        return if (diff <= cacheTime) { // 20 mins
            Log.d("Repo Mediator", "skip initial refresh")
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            Log.d("Repo Mediator", "launch initial refresh")
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepositoryEntity>
    ): MediatorResult {
        Log.d("Repo Mediator", "Fetching users loadType=${loadType}")
        val page: Int = when (loadType) {
            //First load, pager recreated, or invalidate()
            LoadType.REFRESH -> {
                Log.d("Repo Mediator", "REFRESH - Starting from 0")
                0
            }
            //Load next page (scrolling down)
            LoadType.APPEND -> {
                val remoteKey = remoteKeyRepoDao.getRemoteKey(username)
                Log.d("Repo Mediator", "APPEND - remote keys $remoteKey")
                remoteKey?.nextPage ?: return MediatorResult.Success(true)
            }
            //Load previous page (scrolling up) — not always needed
            LoadType.PREPEND -> {
                Log.d("Repo Mediator", "PREPEND - no next key ending pagination")
                return MediatorResult.Success(true)
            }
        }
        return try {
            val response = api.getRepositoryList(
                username = username,
                page = page,
                perPage = state.config.pageSize,
                headers = AuthHelper.getDefaultHeader()
            )
            Log.d("Repo Mediator", "Fetching users page=$page")
            Log.d("Repo Mediator", "Loaded ${response.size} repositories")

            val repoWithTimestamp = response.map { it.toEntity(username) }
            val endOfPagination = response.isEmpty()
            val cacheLastKey = remoteKeyRepoDao.getRemoteKey(username)

            var diff: Long = Integer.MAX_VALUE.toLong()
            if (cacheLastKey != null) {
                val currentTime = System.currentTimeMillis()
                diff = currentTime - cacheLastKey.lastFetched
            }
            val isCacheFresh = cacheLastKey != null && diff < 20 * 60 * 1000

            transactionRunner.runInTransaction {
                if (loadType == LoadType.REFRESH && !isCacheFresh) {
                    remoteKeyRepoDao.clearKeyByUserName(username)
                    repositoryDao.clearDataByUsername(username)
                }
                repositoryDao.insertAll(repoWithTimestamp)
                Log.d("Repo Mediator", "Inserted repos data")

                if (!endOfPagination) {
                    val key = RemoteKeyRepoEntity(
                        username = username,
                        nextPage = page + 1,
                        lastFetched = System.currentTimeMillis()
                    )
                    Log.d("Repo Mediator", "Storing remote key $key")
                    remoteKeyRepoDao.insertOrReplace(key)
                }
            }
            Log.d("Repo Mediator", "Load complete pagination $endOfPagination")
            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}