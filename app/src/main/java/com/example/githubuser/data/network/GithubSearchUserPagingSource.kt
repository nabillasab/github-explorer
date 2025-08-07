package com.example.githubuser.data.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubuser.data.GithubUserData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class GithubSearchUserPagingSource @AssistedInject constructor(
    private val githubApi: GithubApi,
    @Assisted private val query: String
) : PagingSource<Int, GithubUserData>() {

    @AssistedFactory
    interface Factory {
        fun create(query: String): GithubSearchUserPagingSource
    }

    override fun getRefreshKey(state: PagingState<Int, GithubUserData>): Int? = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUserData> {
        return try {
            val page = params.key ?: 1
            val queryMap = mutableMapOf<String, String>()
            queryMap["q"] = query

            val response = githubApi.searchUser(
                page = page,
                perPage = params.loadSize,
                queryMap,
                headers = AuthHelper.getDefaultHeader()
            )
            Log.d("PagingSource", "Fetching users page=$page")
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