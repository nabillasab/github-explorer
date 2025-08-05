package com.example.githubuser.ui.userlist

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.githubuser.R
import com.example.githubuser.domain.GetUserListUseCase
import com.example.githubuser.domain.SearchUserUseCase
import com.example.githubuser.ui.components.ItemListDivider
import com.example.githubuser.ui.components.LoadingScreen
import com.example.githubuser.ui.components.SectionTitle
import com.example.githubuser.ui.components.ToolbarTitle
import com.example.githubuser.ui.components.UserAvatar
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.theme.GithubUserTheme

@Composable
fun GithubUserListScreen(
    onUserClick: (String) -> Unit,
    viewModel: GithubUserListViewModel = hiltViewModel()
) {
    GithubUserList(viewModel, onUserClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GithubUserList(handler: SearchUserHandler, onUserClick: (String) -> Unit) {
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ContentUserList(handler, onUserClick)
        }
    }
}

@Composable
private fun ContentUserList(handler: SearchUserHandler, onUserClick: (String) -> Unit) {
    val context = LocalContext.current
    val searchQuery by handler.searchQuery.collectAsState()
    val users = handler.userPagingFlow.collectAsLazyPagingItems()

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = { handler.onSearchQueryChanged(it) },
            placeholder = { Text("Search user..") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(users.itemCount) { index ->
                users[index]?.let { user ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onUserClick(user.username)
                            }
                    ) {
                        UserItem(user)
                        ItemListDivider()
                    }
                }
            }
        }

        when (users.loadState.refresh) {
            is LoadState.Loading -> {
                LoadingScreen()
            }
            is LoadState.Error -> {
                Toast.makeText(context, "error on load items", Toast.LENGTH_SHORT).show()
            }
            else -> { }
        }
    }
}

@Composable
private fun UserItem(user: User, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        UserAvatar(user.avatarUrl ?: "", 64.dp, modifier = modifier.padding(16.dp))
        SectionTitle(user.username)
    }
}

@Preview(showBackground = true)
@Composable
private fun GithubUserListPreview() {
    GithubUserTheme {
        val fakeHandler = remember { FakeSearchUserHandler() }
        GithubUserList(fakeHandler, onUserClick = { })
    }
}