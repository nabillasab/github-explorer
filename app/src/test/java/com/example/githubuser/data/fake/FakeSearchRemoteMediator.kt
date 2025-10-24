package com.example.githubuser.data.fake

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import com.example.githubuser.data.local.user.UserEntity
import com.example.githubuser.data.mediator.GithubSearchRemoteMediator
import io.mockk.mockk

@OptIn(ExperimentalPagingApi::class)
class FakeSearchRemoteMediator() : GithubSearchRemoteMediator(
    mockk(),
    mockk()
) {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        return MediatorResult.Success(endOfPaginationReached = true)
    }
}
