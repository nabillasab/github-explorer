package com.example.githubuser.ui.userdetail

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.example.githubuser.ui.components.SmallDot
import com.example.githubuser.ui.components.SmallImageDrawable
import com.example.githubuser.ui.components.SmallImageIcon
import com.example.githubuser.ui.components.TextTooltip
import com.example.githubuser.ui.components.UserAvatar
import com.example.githubuser.ui.components.getTime
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.theme.GithubUserTheme
import com.example.githubuser.ui.userdetail.model.Repository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GithubUserDetailScreen(
    navController: NavController,
    username: String,
    onRepoClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

//----------------------------------------------------------
    // TODO REMOVE THIS AFTER GET DATA FROM API

    val user = User(
        username = "nabillasab",
        avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
        fullName = "Nabilla Sabbaha",
        repositories = 6,
        followers = 1,
        following = 12,
        bio = "hidup tak semudah itu bray..."
    )
//----------------------------------------------------------

    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back button"
                    )
                }
            }, title = { Text(username) })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            UserInformation(user)
            FullLineDivider()
            InfoTooltip(
                text = "Only non-forked repositories are displayed",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            RepositoryList(onRepoClick)
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
                CountDetail(12, "repositories")
                Spacer(modifier = Modifier.padding(12.dp))
                CountDetail(12, "followers")
                Spacer(modifier = Modifier.padding(12.dp))
                CountDetail(12, "following")
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
fun RepositoryList(onRepoClick: (String) -> Unit) {
    //======================TODO REMOVE THIS AFTER CONNECT WITH API
    val repos = mutableListOf<Repository>()
    val repository1 = Repository(
        name = "graphql-java",
        description = "GraphQL Java implementation",
        langRepo = "Java",
        star = 10,
        repoUrl = "https://github.com/nabillasab/graphql-java",
        fork = true,
        updatedAt = "2025-07-28T07:46:46Z",
        private = false,
        forksCount = 0,
        licenseName = "MIT License"
    )
    val repository2 = Repository(
        name = "movieproject",
        description = "Android Movie Playground!",
        langRepo = "Kotlin",
        star = 6,
        repoUrl = "https://github.com/nabillasab/movieproject",
        fork = false,
        updatedAt = "2023-11-29T23:19:06Z",
        private = false,
        forksCount = 35,
        licenseName = "MIT License"
    )
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
    repos.add(repository1)
    repos.add(repository2)
    repos.add(repository3)
    repos.add(repository4)
    //================================================================

    LazyColumn {
        items(repos) { repository ->
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
                forksCount.toString(),
                modifier = modifier.padding(start = 4.dp, end = 8.dp)
            )
        }

        if (!licenseName.isNullOrEmpty()) {
            SmallImageDrawable(R.drawable.license_icon, "icon fork")
            LabelMicroText(licenseName, modifier = modifier.padding(start = 4.dp, end = 8.dp))
        }

        LabelMicroText(
            "Updated ${getTime(updatedAt)}",
            modifier = modifier.padding(start = 4.dp, end = 16.dp)
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
            repositories = 10,
            followers = 1,
            following = 12,
            bio = "hidup tak semudah itu bray..."
        )
        GithubUserDetailScreen(rememberNavController(), "nsaudria", onRepoClick = { })
    }
}