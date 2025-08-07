package com.example.githubuser.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.githubuser.data.local.GithubDatabase
import com.example.githubuser.data.local.RemoteKeyRepoEntity
import com.example.githubuser.data.local.RepositoryEntity
import com.example.githubuser.data.network.AuthHelper
import com.example.githubuser.data.network.GithubApi
import com.example.githubuser.data.toEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Repository list pagination with Paging 3 and caching mechanism with Room
 */
@OptIn(ExperimentalPagingApi::class)
class GithubRepoRemoteMediator @AssistedInject constructor(
    private val db: GithubDatabase,
    private val api: GithubApi,
    @Assisted private val username: String
) : RemoteMediator<Int, RepositoryEntity>() {

    @AssistedFactory
    interface Factory {
        fun create(username: String): GithubRepoRemoteMediator
    }

    override suspend fun initialize(): InitializeAction {
        val lastUpdated = db.repositoryDao().getLastUpdated() ?: 0L
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - lastUpdated
        return if (diff > 20 * 6 * 1000) { // 20 mins
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepositoryEntity>
    ): MediatorResult {
        Log.d("Mediator", "Fetching users loadType=${loadType}")
        val page: Int = when (loadType) {
            //First load, pager recreated, or invalidate()
            LoadType.REFRESH -> 1
            //Load next page (scrolling down)
            LoadType.APPEND -> {
                val remoteKey = db.remoteKeyRepoDao().getLastRemoteKey(username)
                remoteKey?.nextPage ?: return MediatorResult.Success(true)
            }
            //Load previous page (scrolling up) — not always needed
            LoadType.PREPEND -> return MediatorResult.Success(true)
        }
        return try {
            val response = api.getRepositoryList(
                username = username,
                page = page,
                perPage = state.config.pageSize,
                headers = AuthHelper.getDefaultHeader()
            )
            Log.d("Mediator", "Fetching users page=$page")
            Log.d("Mediator", "Loaded ${response.size} repositories")

            val repoWithTimestamp = response.map { it.toEntity(username) }
            val endOfPagination = response.isEmpty()
            val cacheLastKey = db.remoteKeyRepoDao().getLastRemoteKey(username)

            var diff: Long = Integer.MAX_VALUE.toLong()
            if (cacheLastKey != null) {
                val currentTime = System.currentTimeMillis()
                diff = currentTime - cacheLastKey.lastFetched
            }
            val isCacheFresh = cacheLastKey != null && diff < 20 * 60 * 1000

            db.withTransaction {
                if (loadType == LoadType.REFRESH && !isCacheFresh) {
                    db.remoteKeyRepoDao().clearKeyByUserName(username)
                    db.repositoryDao().clearDataByUsername(username)
                }
                db.repositoryDao().insertAll(repoWithTimestamp)

                if (!endOfPagination) {
                    val keys = response.map { repo ->
                        RemoteKeyRepoEntity(
                            id = repo.id,
                            username,
                            nextPage = page + 1,
                            lastFetched = System.currentTimeMillis()
                        )
                    }
                    db.remoteKeyRepoDao().insertAll(keys)
                }
            }

            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}