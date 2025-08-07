package com.example.githubuser.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.githubuser.data.local.GithubDatabase
import com.example.githubuser.data.local.RemoteKeyUserEntity
import com.example.githubuser.data.local.UserEntity
import com.example.githubuser.data.network.AuthHelper
import com.example.githubuser.data.network.GithubApi
import com.example.githubuser.data.toEntity
import javax.inject.Inject

/**
 * User list pagination with Paging 3 and caching mechanism with Room
 */
@OptIn(ExperimentalPagingApi::class)
class GithubUserRemoteMediator @Inject constructor(
    private val db: GithubDatabase,
    private val api: GithubApi
) : RemoteMediator<Int, UserEntity>() {

    //timeout caching
    override suspend fun initialize(): InitializeAction {
        val lastUpdated = db.userDao().getLastUpdated() ?: 0L
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
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        val since: Int = when (loadType) {
            //First load, pager recreated, or invalidate()
            LoadType.REFRESH -> 0
            //Load next page (scrolling down)
            LoadType.APPEND -> {
                val remoteKey = db.remoteKeyUserDao().getLastRemoteKey()
                remoteKey?.nextSince ?: return MediatorResult.Success(true)
            }
            //Load previous page (scrolling up) — not always needed
            LoadType.PREPEND -> return MediatorResult.Success(true)
        }

        return try {
            var response = api.getUserList(
                since = since,
                perPage = state.config.pageSize,
                headers = AuthHelper.getDefaultHeader()
            )
            Log.d("Mediator", "Fetching users since=$since")
            val usersWithTimestamp = response.map { it.toEntity() }
            val endOfPagination = response.isEmpty()
            val lastUserId = response.lastOrNull()?.id

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeyUserDao().clearKey()
                    db.userDao().clearAll()
                }
                db.userDao().insertAll(usersWithTimestamp)

                if (!endOfPagination && lastUserId != null) {
                    val keys = response.map { user ->
                        RemoteKeyUserEntity(
                            id = user.id,
                            nextSince = lastUserId,
                            lastFetched = System.currentTimeMillis()
                        )
                    }
                    db.remoteKeyUserDao().insertAll(keys)
                }
            }

            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}