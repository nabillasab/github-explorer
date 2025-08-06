package com.example.githubuser.ui.userdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubuser.data.Result
import com.example.githubuser.domain.GetRepoListUseCase
import com.example.githubuser.domain.GetUserDetailUseCase
import com.example.githubuser.ui.model.UiState
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.userdetail.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubUserDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val getRepoListUseCase: GetRepoListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), UserDetailHandler {

    val username: String = savedStateHandle["username"] ?: ""

    private val _uiState = MutableStateFlow<UiState<User>>(UiState.Loading)
    override val uiState: StateFlow<UiState<User>> = _uiState

    override val repoPagingFlow: Flow<PagingData<Repository>> = getRepoListUseCase.execute(username)
        .cachedIn(viewModelScope)

    init {
        loadUserDetail()
    }

    fun loadUserDetail() {
        viewModelScope.launch {
            getUserDetailUseCase.execute(username).collect { result ->
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