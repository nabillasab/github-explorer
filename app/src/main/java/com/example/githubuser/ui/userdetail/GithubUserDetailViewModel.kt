package com.example.githubuser.ui.userdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.Result
import com.example.githubuser.domain.GetRepoListUseCase
import com.example.githubuser.domain.GetUserDetailUseCase
import com.example.githubuser.ui.model.UiState
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.userdetail.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubUserDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val getRepoListUseCase: GetRepoListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val username: String = savedStateHandle["username"] ?: ""

    private val _uiState = MutableStateFlow<UiState<Pair<User, List<Repository>>>>(UiState.Loading)
    val uiState: StateFlow<UiState<Pair<User, List<Repository>>>> = _uiState

    init {
        loadUserDetail()
    }

    fun loadUserDetail() {
        viewModelScope.launch {
            combine(
                getUserDetailUseCase.execute(username),
                getRepoListUseCase.execute(username)
            ) { user, repos ->
                user.to(repos)
            }.onStart {
                _uiState.value = UiState.Loading
            }.catch { exception ->
                _uiState.value = UiState.Error(exception.message ?: "error")
            }.collect { (user, repos) ->
                if (user is Result.Success && repos is Result.Success) {
                    _uiState.value = UiState.Success(Pair(user.data, repos.data))
                }
            }
        }
    }
}