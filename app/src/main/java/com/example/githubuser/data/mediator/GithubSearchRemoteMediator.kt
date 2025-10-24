package com.example.githubuser.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.githubuser.data.local.GithubDatabase
import com.example.githubuser.data.local.user.RemoteKeySearchEntity
import com.example.githubuser.data.local.user.UserEntity
import com.example.githubuser.data.local.user.UserSourceEntity
import com.example.githubuser.data.network.AuthHelper
import com.example.githubuser.data.network.GithubApi
import com.example.githubuser.data.toEntity
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
open class GithubSearchRemoteMediator @Inject constructor(
    private val db: GithubDatabase,
    private val api: GithubApi
) : RemoteMediator<Int, UserEntity>() {
    private var query: String? = null

    fun setQueryParam(query: String? = null) {
        this.query = query
    }

    // timeout caching
    override suspend fun initialize(): InitializeAction {
        val lastUpdated = db.remoteKeySearchDao().getRemoteKey(query ?: "")?.lastUpdate
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - (lastUpdated ?: 0)
        val cacheTime = 20 * 6 * 1000
        return if (diff <= cacheTime) { // 20 mins
            Log.d("Search Mediator", "skip initial refresh")
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            Log.d("Search Mediator", "launch initial refresh")
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        Log.d("Search Mediator", "load type $loadType")
        val loadKey: Int = when (loadType) {
            LoadType.REFRESH -> {
                Log.d("Search Mediator", "REFRESH - Starting from 1")
                1
            }
            // Load next page (scrolling down)
            LoadType.APPEND -> {
                val remoteKey = db.remoteKeySearchDao().getRemoteKey(query ?: "")
                Log.d("Search Mediator", "APPEND - remote keys $remoteKey")
                remoteKey?.nextPage ?: return MediatorResult.Success(true)
            }
            // Load previous page (scrolling up) — not always needed
            LoadType.PREPEND -> {
                Log.d("Search Mediator", "PREPEND - no next key ending pagination")
                return MediatorResult.Success(true)
            }
        }

        Log.d(
            "Search Mediator",
            "Fetching data from server since $loadKey, ${state.config.pageSize}"
        )
        return try {
            val response = api.searchUser(
                page = loadKey,
                perPage = state.config.pageSize,
                query = query ?: "",
                headers = AuthHelper.getDefaultHeader()
            )
            val result = response.users.map { it.toEntity() }

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    Log.d("Search Mediator", "Clearing source dao and remote key search dao")
                    db.userSourceDao().clearSearchSources(query ?: "")
                    db.remoteKeySearchDao().clearKey(query ?: "")

                    // clean up stale data when refreshing
                    val aWeekAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
                    db.userSourceDao().clearStaleData(aWeekAgo)
                }
                db.userDao().insertAll(result)
                Log.d("Search Mediator", "Inserted ${result.size} item to main list")

                val currCount = if (loadType == LoadType.REFRESH) {
                    0
                } else {
                    db.userSourceDao().getSearchListCount(query ?: "")
                }
                Log.d("Search Mediator", "Current count $currCount")

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
                db.userSourceDao().insertSources(sources)
                Log.d("Search Mediator", "Inserted sources")

                // update remote keys
                db.remoteKeySearchDao().insertOrReplace(
                    RemoteKeySearchEntity(
                        searchQuery = query ?: "",
                        nextPage = if (result.isEmpty()) null else loadKey + 1,
                        lastUpdate = System.currentTimeMillis()
                    )
                )
            }
            Log.d("Search Mediator", "Load complete pagination ${result.isEmpty()}")
            MediatorResult.Success(endOfPaginationReached = result.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
