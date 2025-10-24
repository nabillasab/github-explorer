package com.example.githubuser.ui.userlist

import androidx.paging.PagingData
import com.example.githubuser.ui.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SearchUserHandler {

    val searchQuery: StateFlow<String>
    val userPagingFlow: Flow<PagingData<User>>
    fun onSearchQueryChanged(query: String)
}
