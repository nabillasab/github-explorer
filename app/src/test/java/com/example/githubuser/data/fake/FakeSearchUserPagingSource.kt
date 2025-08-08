package com.example.githubuser.data.fake

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingState
import com.example.githubuser.data.GithubUserData
import com.example.githubuser.data.network.GithubSearchUserPagingSource
import io.mockk.mockk

@OptIn(ExperimentalPagingApi::class)
class FakeSearchUserPagingSource(private val data: List<GithubUserData>) :
    GithubSearchUserPagingSource(mockk(), "") {

    override fun getRefreshKey(state: PagingState<Int, GithubUserData>): Int? {
        return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUserData> {
        return LoadResult.Page(data = data, prevKey = null, nextKey = null)
    }
}