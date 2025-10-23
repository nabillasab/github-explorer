package com.example.githubuser.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.githubuser.data.local.user.RemoteKeyUserDao
import com.example.githubuser.data.local.user.RemoteKeyUserEntity
import com.example.githubuser.data.local.user.UserDao
import com.example.githubuser.data.local.user.UserEntity
import com.example.githubuser.data.local.user.UserSourceDao
import com.example.githubuser.data.local.user.UserSourceEntity
import com.example.githubuser.data.network.AuthHelper
import com.example.githubuser.data.network.GithubApi
import com.example.githubuser.data.toEntity
import javax.inject.Inject

/**
 * User list pagination with Paging 3 and caching mechanism with Room
 */
@OptIn(ExperimentalPagingApi::class)
open class GithubUserRemoteMediator @Inject constructor(
    private val transactionRunner: RoomTransactionRunner,
    private val userDao: UserDao,
    private val userSourceDao: UserSourceDao,
    private val remoteKeyUserDao: RemoteKeyUserDao,
    private val api: GithubApi,
) : RemoteMediator<Int, UserEntity>() {

    //timeout caching
    override suspend fun initialize(): InitializeAction {
        val lastUpdated = userDao.getLastUpdated() ?: 0L
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - lastUpdated
        val cacheTime = 20 * 6 * 1000
        return if (diff <= cacheTime) { // 20 mins
            Log.d("User Mediator", "skip initial refresh")
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            Log.d("User Mediator", "launch initial refresh")
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        Log.d("User Mediator", "load type $loadType")
        val loadKey: Int = when (loadType) {
            LoadType.REFRESH -> {
                Log.d("User Mediator", "REFRESH - Starting from 0")
                0
            }

            //Load next page (scrolling down)
            LoadType.APPEND -> {
                val remoteKey = remoteKeyUserDao.getRemoteKey()
                Log.d("User Mediator", "APPEND - remote keys $remoteKey")
                remoteKey?.nextKey ?: return MediatorResult.Success(true)
            }
            //Load previous page (scrolling up) — not always needed
            LoadType.PREPEND -> {
                Log.d("User Mediator", "PREPEND - no next key ending pagination")
                return MediatorResult.Success(true)
            }
        }

        Log.d("User Mediator", "Fetching data from server since $loadKey, ${state.config.pageSize}")
        return try {
            val response = api.getUserList(
                since = loadKey,
                perPage = state.config.pageSize,
                headers = AuthHelper.getDefaultHeader()
            )
            val result = response.map { it.toEntity() }

            transactionRunner.runInTransaction {
                if (loadType == LoadType.REFRESH) {
                    Log.d("User Mediator", "Clearing main list")
                    userSourceDao.clearMainListSources()
                    remoteKeyUserDao.clearKey()
                }
                userDao.insertAll(result)
                Log.d("User Mediator", "Inserted ${result.size} item to main list")

                val currCount = if (loadType == LoadType.REFRESH) {
                    0
                } else {
                    userSourceDao.getMainListCount()
                }
                Log.d("User Mediator", "Current count $currCount")

                //insert data to source
                val sources = result.mapIndexed { index, entity ->
                    UserSourceEntity(
                        id = entity.id,
                        userId = entity.userName,
                        sourceType = "MAIN_LIST",
                        searchQuery = null,
                        position = currCount + index,
                        timestamp = entity.lastUpdated
                    )
                }
                userSourceDao.insertSources(sources)
                Log.d("User Mediator", "Inserted sources")

                //store last data for pagination
                val nextSince = result.lastOrNull()?.id
                if (nextSince != null) {
                    Log.d("User Mediator", "Storing key next $nextSince")
                    remoteKeyUserDao.insertOrReplace(

                        RemoteKeyUserEntity(
                            id = "user_list",
                            nextKey = nextSince,
                            lastUpdate = System.currentTimeMillis()
                        )
                    )
                }
            }
            Log.d("User Mediator", "Load complete pagination ${result.isEmpty()}")
            MediatorResult.Success(endOfPaginationReached = result.isEmpty())
        } catch (e: Exception) {
            Log.d("User Mediator", "error ${e.message}")
            MediatorResult.Error(e)
        }
    }
}