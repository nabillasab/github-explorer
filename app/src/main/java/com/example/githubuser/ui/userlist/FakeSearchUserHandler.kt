package com.example.githubuser.ui.userlist

import androidx.paging.PagingData
import com.example.githubuser.ui.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

class FakeSearchUserHandler() : SearchUserHandler {

    fun getFakeUserList(): List<User> {
        val userList = mutableListOf<User>()
        val user1 = User(
            username = "nabillasab",
            avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
            fullName = "Nabilla Sabbaha",
            followers = 1,
            following = 12,
            repoCount = 11,
            bio = null
        )
        val user2 = User(
            username = "audrians",
            avatarUrl = "https://avatars.githubusercontent.com/u/6389222?v=4",
            fullName = "Nabilla Sabbaha",
            followers = 4,
            following = 22,
            repoCount = 12,
            bio = null
        )
        userList.add(user1)
        userList.add(user2)
        return userList
    }

    override val searchQuery: StateFlow<String> = MutableStateFlow("")

    override val userPagingFlow: Flow<PagingData<User>> =
        flowOf(PagingData.from(getFakeUserList()))

    override fun onSearchQueryChanged(query: String) {
        //no op for preview
    }
}