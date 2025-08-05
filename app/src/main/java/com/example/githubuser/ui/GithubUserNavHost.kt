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
import com.example.githubuser.ui.userdetail.GithubUserDetailScreen
import com.example.githubuser.ui.userlist.GithubUserListScreen

@Composable
fun GithubUserNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    NavHost(
        navController = navHostController,
        startDestination = "github_user_list",
        modifier = modifier
    ) {
        composable("github_user_list") {
            GithubUserListScreen(onUserClick = { username ->
                navHostController.navigate("detail/$username")
            })
        }

        composable(
            route = "detail/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { navBackStackEntry ->
            GithubUserDetailScreen(navHostController, onRepoClick = { repoUrl ->
                //TODO redirect to webview page based on repository URL
                Toast.makeText(context, repoUrl, Toast.LENGTH_SHORT).show()
            })
        }
    }

}