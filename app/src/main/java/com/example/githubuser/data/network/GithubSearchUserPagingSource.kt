package com.example.githubuser.data.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubuser.data.GithubUserData
import javax.inject.Inject

class GithubSearchUserPagingSource @Inject constructor(
    private val githubApi: GithubApi
) : PagingSource<Int, GithubUserData>() {

    var query = ""

    fun setQueryText(query: String) {
        this.query = query
    }

    override fun getRefreshKey(state: PagingState<Int, GithubUserData>): Int? = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUserData> {
        return try {
            val since = params.key ?: 0
            val queryMap = mutableMapOf<String, String>()
            queryMap["q"] = query

            val response = githubApi.searchUser(
                since = since,
                perPage = params.loadSize,
                queryMap,
                headers = AuthHelper.getDefaultHeader()
            )
            Log.d("PagingSource", "Fetching users since=$since")
            val isEndOfList = response.users.size < params.loadSize || response.users.isEmpty()
            LoadResult.Page(
                data = response.users,
                prevKey = null,
                nextKey = if (isEndOfList) null else response.users.lastOrNull()?.id?.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}