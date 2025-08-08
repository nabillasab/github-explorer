package com.example.githubuser.helper

import androidx.paging.PagingSource
import androidx.paging.PagingState

class ListPagingSource<T : Any>(private val data: List<T>) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>) = null
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> =
        LoadResult.Page(data, prevKey = null, nextKey = null)
}