package com.example.githubuser.data.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubuser.data.GithubUserData
import javax.inject.Inject

@Deprecated("use GithubUserRemoteMediator instead for caching mechanism with room")
/**
 * User list pagination with Paging 3
 */
class GithubUserPagingSource @Inject constructor(
    private val githubApi: GithubApi
) : PagingSource<Int, GithubUserData>() {

    override fun getRefreshKey(state: PagingState<Int, GithubUserData>): Int? = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUserData> {
        return try {
            val since = params.key ?: 0
            val response = githubApi.getUserList(
                since = since,
                perPage = params.loadSize,
                headers = AuthHelper.getDefaultHeader()
            )
            Log.d("PagingSource", "Fetching users since=$since")
            val isEndOfList = response.size < params.loadSize || response.isEmpty()
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = if (isEndOfList) null else response.lastOrNull()?.id
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}