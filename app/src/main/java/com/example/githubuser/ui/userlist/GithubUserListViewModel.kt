package com.example.githubuser.ui.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubuser.domain.GithubUserRepository
import com.example.githubuser.ui.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest

@HiltViewModel
class GithubUserListViewModel @Inject constructor(
    private val repository: GithubUserRepository
) : ViewModel(), SearchUserHandler {

    private val _searchQuery = MutableStateFlow("")
    override val searchQuery: StateFlow<String> = _searchQuery

    override fun onSearchQueryChanged(queryStr: String) {
        val query = queryStr.trim().lowercase()
        _searchQuery.value = query
    }

    // debounce for waiting until user pauses typing in ms
    // distinctUntilChanged for avoid duplicate queries
    // flatMapLatest for cancel old request, keep latest
    // cachedIn for cache result across config changes
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override val userPagingFlow: Flow<PagingData<User>> =
        _searchQuery.debounce { 500 }.distinctUntilChanged().flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getUserList()
            } else {
                repository.getUserByUsername(query)
            }
        }.cachedIn(viewModelScope)
}
