package com.example.githubuser.ui.userdetail

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.githubuser.R
import com.example.githubuser.ui.components.BodyLargeText
import com.example.githubuser.ui.components.BodyMediumText
import com.example.githubuser.ui.components.BodyText
import com.example.githubuser.ui.components.ErrorFooter
import com.example.githubuser.ui.components.ErrorLoadScreen
import com.example.githubuser.ui.components.ImageDrawable
import com.example.githubuser.ui.components.ImageIcon
import com.example.githubuser.ui.components.LabelMediumText
import com.example.githubuser.ui.components.LabelMicroText
import com.example.githubuser.ui.components.LabelSmallText
import com.example.githubuser.ui.components.LanguageColors
import com.example.githubuser.ui.components.LoadingScreen
import com.example.githubuser.ui.components.SectionTitle
import com.example.githubuser.ui.components.SmallDot
import com.example.githubuser.ui.components.TextTooltip
import com.example.githubuser.ui.components.UserAvatar
import com.example.githubuser.ui.components.getTime
import com.example.githubuser.ui.model.UiState
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.theme.GithubUserTheme
import com.example.githubuser.ui.theme.MetadataColor
import com.example.githubuser.ui.theme.PrimaryColor
import com.example.githubuser.ui.theme.RepoDescriptionColor
import com.example.githubuser.ui.theme.RepoTitleColor
import com.example.githubuser.ui.theme.White
import com.example.githubuser.ui.userdetail.model.Repository

@Composable
fun GithubUserDetailScreen(
    navController: NavController,
    username: String,
    onRepoClick: (Pair<String, String>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GithubUserDetailViewModel = hiltViewModel()
) {
    GithubUserDetail(navController, username, onRepoClick, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GithubUserDetail(
    navController: NavController,
    username: String,
    onRepoClick: (Pair<String, String>) -> Unit,
    handler: UserDetailHandler
) {
    val repos = handler.repoPagingFlow.collectAsLazyPagingItems()
    val uiState by handler.uiState.collectAsState()

    //force paging 3 to refresh to fix issue it doesn't trigger refresh
    LaunchedEffect(Unit) { repos.refresh() }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = PrimaryColor,
                    navigationIconContentColor = PrimaryColor,
                    titleContentColor = PrimaryColor,
                    actionIconContentColor = PrimaryColor
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back button"
                        )
                    }
                },
                title = {
                    SectionTitle(
                        username,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryColor
                    )
                })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            DetailHeader(uiState)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(White)
                    .shadow(1.dp)
            )
            RepositoryList(repos, onRepoClick)
        }
    }
}

@Composable
fun DetailHeader(uiState: UiState<User>) {
    val context = LocalContext.current
    when (uiState) {
        is UiState.Loading -> {
            LoadingScreen()
        }

        is UiState.Success -> {
            UserInformation((uiState as UiState.Success<User>).data)
        }

        is UiState.Error -> {
            val userEmpty = User(
                id = 0,
                username = "",
                avatarUrl = "",
                fullName = "not found",
                repoCount = 0,
                following = 0,
                followers = 0,
                bio = ""
            )
            UserInformation(userEmpty)
            Toast.makeText(context, (uiState as UiState.Error).message, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun UserInformation(user: User, modifier: Modifier = Modifier) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row {
            UserAvatar(
                user.avatarUrl ?: "", 64.dp, modifier = modifier.padding(
                    start = 16.dp, end = 16.dp, bottom = 8.dp
                )
            )
            Column {
                BodyLargeText(
                    user.fullName ?: "",
                    modifier = modifier.padding(bottom = 8.dp),
                    color = PrimaryColor
                )
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
                user.bio, maxLines = 3, modifier = Modifier.padding(
                    horizontal = 16.dp, vertical = 8.dp
                ), color = PrimaryColor
            )
        }
    }
}

@Composable
fun CountDetail(count: Int, title: String) {
    Column {
        LabelMediumText(count.toString(), fontWeight = FontWeight.Bold, color = PrimaryColor)
        LabelSmallText(title, color = PrimaryColor)
    }
}

@Composable
fun RepositoryList(
    repositoryList: LazyPagingItems<Repository>, onRepoClick: (Pair<String, String>) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
        items(repositoryList.itemCount) { index ->
            repositoryList[index]?.let { repository ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onRepoClick(Pair(repository.name, repository.repoUrl))
                        }) {
                    if (!repository.fork) {
                        ItemRepository(repository)
                    }
                }
            }
        }
        val loadState = repositoryList.loadState
        if (loadState.append is LoadState.Error) {
            val error = loadState.append as LoadState.Error
            item {
                ErrorFooter(
                    errorMsg = error.error.localizedMessage ?: "Failed to load more",
                    onRetry = { repositoryList.retry() })
            }
        }
    }

    when (repositoryList.loadState.refresh) {
        is LoadState.Loading -> {
            LoadingScreen()
        }

        is LoadState.Error -> {
            val error = repositoryList.loadState.refresh as LoadState.Error
            ErrorLoadScreen(
                error.error.message ?: "Failed to load data", onRetry = { repositoryList.retry() })
        }

        else -> {}
    }
}

@Composable
fun ItemRepository(repository: Repository) {
    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, Color(0xFFE5E5E5)),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BodyMediumText(
                    text = repository.name,
                    color = RepoTitleColor,
                    modifier = Modifier.padding(end = 8.dp)
                )

                if (repository.private) {
                    TextTooltip("Private")
                }
            }
            if (!repository.description.isNullOrEmpty()) {
                BodyText(
                    repository.description,
                    color = RepoDescriptionColor,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            LabelMicroText(
                "Updated ${getTime(repository.updatedAt)}",
                color = Color(0xFF8B949E),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            Spacer(modifier = Modifier.size(4.dp))
            RepoDetail(
                repository.langRepo,
                repository.star,
                repository.forksCount,
                repository.licenseName,
            )
        }
    }
}

@Composable
fun RepoDetail(
    langRepo: String?,
    star: Int,
    forksCount: Int,
    licenseName: String?,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        if (!langRepo.isNullOrEmpty()) {
            val color = LanguageColors.getColors(langRepo)
            SmallDot(color = color)
            LabelMicroText(
                langRepo,
                color = color,
                modifier = modifier.padding(start = 4.dp, end = 12.dp)
            )
        }

        if (star > 0) {
            ImageIcon(
                Icons.Default.Star,
                "icon star",
                color = MetadataColor
            )
            LabelMicroText(
                star.toString(),
                modifier = modifier.padding(start = 4.dp, end = 12.dp)
            )
        }

        if (forksCount > 0) {
            ImageDrawable(
                R.drawable.git_fork,
                "icon fork",
                color = MetadataColor
            )
            LabelMicroText(
                forksCount.toString(), modifier = modifier.padding(start = 4.dp, end = 12.dp)
            )
        }

        if (!licenseName.isNullOrEmpty()) {
            ImageDrawable(
                R.drawable.license_icon, "icon fork",
                color = MetadataColor
            )
            LabelMicroText(licenseName, modifier = modifier.padding(start = 4.dp, end = 12.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GithubUserListPreview() {
    GithubUserTheme {
        val fakeHandler = remember { FakeUserDetailHandler() }
        GithubUserDetail(rememberNavController(), "nabillasab", onRepoClick = { }, fakeHandler)
    }
}

@Preview(showBackground = true)
@Composable
fun ItemRpositoryPreview() {
    GithubUserTheme {
        ItemRepository(FakeUserDetailHandler().getFakeRepoList()[0])
    }
}
