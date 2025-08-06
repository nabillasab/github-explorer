package com.example.githubuser.ui.userdetail

import androidx.paging.PagingData
import com.example.githubuser.ui.model.UiState
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.userdetail.model.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserDetailHandler {
    val uiState: StateFlow<UiState<User>>
    val repoPagingFlow: Flow<PagingData<Repository>>
}