package com.example.githubuser.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.githubuser.ui.repository.RepositoryWebviewScreen
import com.example.githubuser.ui.userdetail.GithubUserDetailScreen
import com.example.githubuser.ui.userlist.GithubUserListScreen

@Composable
fun GithubUserNavHost(
    navHostController: NavHostController, modifier: Modifier = Modifier
) {
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
            GithubUserDetailScreen(navHostController, onRepoClick = { repoDetail ->
                val name = Uri.encode(repoDetail.first)
                val encodedUrl = Uri.encode(repoDetail.second)
                navHostController.navigate("repository/$name/$encodedUrl")
            })
        }

        composable(
            route = "repository/{name}/{encodedUrl}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("encodedUrl") { type = NavType.StringType },
            )
        ) { navBackStackEntry ->
            val name = navBackStackEntry.arguments?.getString("name") ?: ""
            val url = navBackStackEntry.arguments?.getString("encodedUrl") ?: ""
            RepositoryWebviewScreen(navHostController, url, name)
        }
    }

}