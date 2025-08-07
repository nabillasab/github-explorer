package com.example.githubuser.data.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubuser.data.GithubRepositoryData
import javax.inject.Inject

@Deprecated("use GithubRepoRemoteMediator instead for caching mechanism with room")
/**
 * Repository list pagination with Paging 3
 */
class GithubRepositoryPagingSource @Inject constructor(
    private val githubApi: GithubApi
) : PagingSource<Int, GithubRepositoryData>() {

    var username = ""

    fun setUserName(username: String) {
        this.username = username
    }

    override fun getRefreshKey(state: PagingState<Int, GithubRepositoryData>): Int? {
        return state.anchorPosition?.let { anchorpos ->
            state.closestPageToPosition(anchorpos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorpos)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubRepositoryData> {
        return try {
            val page = params.key ?: 1
            val response = githubApi.getRepositoryList(
                username = username,
                page = page,
                perPage = params.loadSize,
                headers = AuthHelper.getDefaultHeader()
            )
            Log.d("Mediator", "Fetching users page=$page")
            val isEndOfList = response.size < params.loadSize || response.isEmpty()
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = if (isEndOfList) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}