package com.example.githubuser.helper

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import com.example.githubuser.data.local.UserEntity

suspend fun PagingSource<Int, UserEntity>.loadAll(): List<UserEntity> {
    val result = this.load(
        LoadParams.Refresh(
            key = null,
            loadSize = 20,
            placeholdersEnabled = false
        )
    )
    return if (result is LoadResult.Page) {
        result.data
    } else {
        emptyList()
    }
}