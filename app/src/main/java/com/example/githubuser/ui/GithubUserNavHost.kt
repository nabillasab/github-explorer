package com.example.githubuser.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.userdetail.GithubUserDetailScreen
import com.example.githubuser.ui.userlist.GithubUserListScreen

@Composable
fun GithubUserNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userList = mutableListOf<User>()
    val user1 = User(
        username = "nabillasab",
        avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
        fullName = "Nabilla Sabbaha",
        repositories = 1,
        followers = 1,
        following = 12,
        bio = null
    )
    val user2 = User(
        username = "audrians",
        avatarUrl = "https://avatars.githubusercontent.com/u/6389222?v=4",
        fullName = "Nabilla Sabbaha",
        repositories = 5,
        followers = 4,
        following = 22,
        bio = null
    )
    val user3 = User(
        username = "bilski",
        avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
        fullName = "Nabilla Sabbaha",
        repositories = 6,
        followers = 1,
        following = 12,
        bio = null
    )
    val user4 = User(
        username = "nsaudria",
        avatarUrl = "https://avatars.githubusercontent.com/u/6389222?v=4",
        fullName = "Nabilla Sabbaha",
        repositories = 100,
        followers = 4,
        following = 22,
        bio = null
    )
    userList.add(user1)
    userList.add(user2)
    userList.add(user3)
    userList.add(user4)

    NavHost(
        navController = navHostController,
        startDestination = "github_user_list",
        modifier = modifier
    ) {
        composable("github_user_list") {
            GithubUserListScreen(userList, onUserClick = { username ->
                navHostController.navigate("detail/$username")
            })
        }

        composable(
            route = "detail/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val username = navBackStackEntry.arguments?.getString("username")
            GithubUserDetailScreen(navHostController, username ?: "", onRepoClick = { repoUrl ->
                //TODO redirect to webview page based on repository URL
                Toast.makeText(context, repoUrl, Toast.LENGTH_SHORT).show()
            })
        }
    }

}