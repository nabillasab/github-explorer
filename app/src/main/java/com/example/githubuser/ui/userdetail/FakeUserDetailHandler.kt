package com.example.githubuser.ui.userdetail

import androidx.paging.PagingData
import com.example.githubuser.ui.model.UiState
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.userdetail.model.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

class FakeUserDetailHandler : UserDetailHandler {

    fun getFakeRepoList(): List<Repository> {
        val repos = mutableListOf<Repository>()
        val repository3 = Repository(
            id = 1,
            name = "movieproject",
            description = "Android Movie Playground!asdbajbdjabdjah ajkdbajhbda kjadkjabsdjkasbdjaksbdjasbdjkasbdjkasbd askjdbakjsbdkjasbd",
            langRepo = null,
            star = 6,
            repoUrl = "https://github.com/nabillasab/movieproject",
            fork = false,
            updatedAt = "2025-07-28T07:46:46Z",
            private = false,
            forksCount = 0,
            licenseName = "MIT License"
        )
        val repository4 = Repository(
            id = 2,
            name = "movieproject",
            description = null,
            langRepo = null,
            star = 0,
            repoUrl = "https://github.com/nabillasab/movieproject",
            fork = false,
            updatedAt = "2025-07-28T07:46:46Z",
            private = false,
            forksCount = 3,
            licenseName = null
        )
        repos.add(repository3)
        repos.add(repository4)
        return repos
    }

    override val uiState: StateFlow<UiState<User>>
        get() = MutableStateFlow(
            UiState.Success(
                User(
                    id = 1,
                    username = "nabillasab",
                    avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
                    fullName = "Nabilla Sabbaha",
                    repoCount = 10,
                    followers = 1,
                    following = 12,
                    bio = "hidup tak semudah itu bray..."
                )
            )
        )

    override val repoPagingFlow: Flow<PagingData<Repository>>
        get() = flowOf(PagingData.from(getFakeRepoList()))
}
