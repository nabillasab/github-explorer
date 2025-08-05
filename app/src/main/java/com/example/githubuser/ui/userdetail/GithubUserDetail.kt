package com.example.githubuser.ui.userdetail

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.githubuser.R
import com.example.githubuser.ui.components.BodyText
import com.example.githubuser.ui.components.FullLineDivider
import com.example.githubuser.ui.components.InfoTooltip
import com.example.githubuser.ui.components.ItemListDivider
import com.example.githubuser.ui.components.LabelLargeText
import com.example.githubuser.ui.components.LabelMediumText
import com.example.githubuser.ui.components.LabelMicroText
import com.example.githubuser.ui.components.LabelSmallText
import com.example.githubuser.ui.components.LoadingScreen
import com.example.githubuser.ui.components.SmallDot
import com.example.githubuser.ui.components.SmallImageDrawable
import com.example.githubuser.ui.components.SmallImageIcon
import com.example.githubuser.ui.components.TextTooltip
import com.example.githubuser.ui.components.UserAvatar
import com.example.githubuser.ui.components.getTime
import com.example.githubuser.ui.model.UiState
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.theme.GithubUserTheme
import com.example.githubuser.ui.userdetail.model.Repository

@Composable
fun GithubUserDetailScreen(
    navController: NavController,
    onRepoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GithubUserDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    when (uiState) {
        is UiState.Loading -> {
            LoadingScreen()
        }

        is UiState.Success -> {
            GithubUserDetail(
                navController,
                (uiState as UiState.Success<Pair<User, List<Repository>>>).data,
                onRepoClick
            )
        }

        is UiState.Error -> {
            Toast.makeText(context, (uiState as UiState.Error).message, Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GithubUserDetail(
    navController: NavController,
    userDetail: Pair<User, List<Repository>>,
    onRepoClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back button"
                    )
                }
            }, title = { Text(userDetail.first.username) })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            UserInformation(userDetail.first)
            FullLineDivider()
            InfoTooltip(
                text = "Only non-forked repositories are displayed",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            RepositoryList(userDetail.second, onRepoClick)
        }
    }
}

@Composable
fun UserInformation(user: User, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        UserAvatar(
            user.avatarUrl ?: "", 64.dp, modifier = modifier.padding(
                start = 16.dp, end = 16.dp, bottom = 8.dp
            )
        )
        Column {
            LabelMediumText(user.fullName ?: "", modifier = modifier)
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                CountDetail(user.repoCount, "repositories")
                Spacer(modifier = Modifier.padding(12.dp))
                CountDetail(user.followers, "followers")
                Spacer(modifier = Modifier.padding(12.dp))
                CountDetail(user.following, "following")
            }
        }
    }
    if (!user.bio.isNullOrEmpty()) {
        LabelSmallText(
            user.bio, modifier = Modifier.padding(
                start = 16.dp, end = 16.dp, bottom = 8.dp
            )
        )
    }
}

@Composable
fun CountDetail(count: Int, title: String) {
    Column {
        LabelMediumText(count.toString())
        LabelSmallText(title)
    }
}

@Composable
fun RepositoryList(repositoryList: List<Repository>, onRepoClick: (String) -> Unit) {
    LazyColumn {
        items(repositoryList) { repository ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onRepoClick(repository.repoUrl)
                    }) {
                if (!repository.fork) {
                    ItemRepository(repository)
                    ItemListDivider()
                }
            }
        }
    }
}

@Composable
fun ItemRepository(repository: Repository) {
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SmallImageDrawable(R.drawable.repository, "repository", size = 16.dp)
            LabelLargeText(repository.name, modifier = Modifier.padding(start = 4.dp, end = 8.dp))
            TextTooltip(if (repository.private) "Private" else "Public")
        }
        if (!repository.description.isNullOrEmpty()) {
            BodyText(repository.description, modifier = Modifier.padding(horizontal = 16.dp))
        }
        Spacer(modifier = Modifier.size(4.dp))
        RepoDetail(
            repository.langRepo,
            repository.updatedAt,
            repository.star,
            repository.forksCount,
            repository.licenseName,
        )
    }
}

@Composable
fun RepoDetail(
    langRepo: String?,
    updatedAt: String,
    star: Int,
    forksCount: Int,
    licenseName: String?,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        if (!langRepo.isNullOrEmpty()) {
            SmallDot()
            LabelMicroText(langRepo, modifier = modifier.padding(start = 4.dp, end = 8.dp))
        }

        if (star > 0) {
            SmallImageIcon(Icons.Default.Star, "icon star")
            LabelMicroText(star.toString(), modifier = modifier.padding(start = 4.dp, end = 8.dp))
        }

        if (forksCount > 0) {
            SmallImageDrawable(R.drawable.git_fork, "icon fork")
            LabelMicroText(
                forksCount.toString(), modifier = modifier.padding(start = 4.dp, end = 8.dp)
            )
        }

        if (!licenseName.isNullOrEmpty()) {
            SmallImageDrawable(R.drawable.license_icon, "icon fork")
            LabelMicroText(licenseName, modifier = modifier.padding(start = 4.dp, end = 8.dp))
        }

        LabelMicroText(
            "Updated ${getTime(updatedAt)}", modifier = modifier.padding(start = 4.dp, end = 16.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GithubUserListPreview() {
    GithubUserTheme {
        val user1 = User(
            username = "nabillasab",
            avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
            fullName = "Nabilla Sabbaha",
            repoCount = 10,
            followers = 1,
            following = 12,
            bio = "hidup tak semudah itu bray..."
        )
        val repos = mutableListOf<Repository>()
        val repository3 = Repository(
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
        GithubUserDetail(rememberNavController(), Pair(user1, repos), onRepoClick = { })
    }
}