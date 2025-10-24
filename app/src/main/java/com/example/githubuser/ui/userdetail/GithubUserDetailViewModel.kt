package com.example.githubuser.ui.userdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubuser.data.Result
import com.example.githubuser.domain.GithubUserRepository
import com.example.githubuser.ui.GithubUserDestinationArgs
import com.example.githubuser.ui.model.UiState
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.userdetail.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GithubUserDetailViewModel @Inject constructor(
    private val repository: GithubUserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), UserDetailHandler {

    val username: String = savedStateHandle[GithubUserDestinationArgs.USERNAME_ARG] ?: ""

    private val _uiState = MutableStateFlow<UiState<User>>(UiState.Loading)
    override val uiState: StateFlow<UiState<User>> = _uiState

    override val repoPagingFlow: Flow<PagingData<Repository>> = repository.getRepoList(username)
        .cachedIn(viewModelScope)

    init {
        loadUserDetail()
    }

    fun loadUserDetail() {
        viewModelScope.launch {
            repository.getUserDetail(username).collect { result ->
                val state = when (result) {
                    is Result.Loading -> UiState.Loading
                    is Result.Success -> UiState.Success(result.data)
                    is Result.Error -> UiState.Error(result.message)
                }
                _uiState.value = state
            }
        }
    }
}
