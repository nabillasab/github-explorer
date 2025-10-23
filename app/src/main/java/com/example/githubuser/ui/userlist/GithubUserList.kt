package com.example.githubuser.ui.userlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.githubuser.R
import com.example.githubuser.ui.components.ErrorFooter
import com.example.githubuser.ui.components.ErrorLoadScreen
import com.example.githubuser.ui.components.ItemListDivider
import com.example.githubuser.ui.components.LoadingScreen
import com.example.githubuser.ui.components.SectionTitle
import com.example.githubuser.ui.components.ToolbarTitle
import com.example.githubuser.ui.components.UserAvatar
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.theme.GithubUserTheme

@Composable
fun GithubUserListScreen(
    onUserClick: (String) -> Unit, viewModel: GithubUserListViewModel = hiltViewModel()
) {
    GithubUserList(viewModel, onUserClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GithubUserList(
    handler: SearchUserHandler, onUserClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.github_icon),
                        contentDescription = "github icon",
                        modifier = Modifier.size(36.dp)
                    )
                },
                title = { ToolbarTitle("Github Users") },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            ContentUserList(handler, onUserClick)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentUserList(
    handler: SearchUserHandler, onUserClick: (String) -> Unit
) {
    val searchQuery by handler.searchQuery.collectAsState()
    val users = handler.userPagingFlow.collectAsLazyPagingItems()

    Column {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = searchQuery,
            onQueryChange = { handler.onSearchQueryChanged(it) },
            onSearch = { },
            active = false,
            onActiveChange = { },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "Search Icon"
                )
            },
            placeholder = {
                Text("Search name here…")
            }
        ) {

        }


        Spacer(modifier = Modifier.height(8.dp))

        val loadState = users.loadState
        when {
            loadState.refresh is LoadState.Loading -> {
                LoadingScreen()
            }

            loadState.refresh is LoadState.Error -> {
                val error = users.loadState.refresh as LoadState.Error
                ErrorLoadScreen(
                    error.error.message ?: "Failed to load data", onRetry = { users.retry() })
            }

            users.itemCount == 0 -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text("No users found")
                }
            }

            else -> {
                LazyColumn {
                    items(users.itemCount) { index ->
                        users[index]?.let { user ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onUserClick(user.username)
                                    }) {
                                UserItem(user)
                            }
                        }
                    }

                    item {
                        if (users.loadState.append is LoadState.Loading) {
                            LoadingScreen()
                        } else if (users.loadState.append is LoadState.Error) {
                            val error = loadState.append as LoadState.Error
                            ErrorFooter(
                                errorMsg = error.error.localizedMessage ?: "Failed to load more",
                                onRetry = { users.retry() })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(user: User, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        UserAvatar(user.avatarUrl ?: "", 64.dp, modifier = modifier.padding(16.dp))
        SectionTitle(user.username)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun GithubUserListPreview() {
    GithubUserTheme {
        val fakeHandler = remember { FakeSearchUserHandler() }
        GithubUserList(fakeHandler, onUserClick = { })
    }
}