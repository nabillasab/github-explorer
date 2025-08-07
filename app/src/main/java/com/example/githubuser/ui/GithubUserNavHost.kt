package com.example.githubuser.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.githubuser.ui.GithubUserDestinationArgs.NAME_ARG
import com.example.githubuser.ui.GithubUserDestinationArgs.REPO_URL_ARG
import com.example.githubuser.ui.GithubUserDestinationArgs.USERNAME_ARG
import com.example.githubuser.ui.repository.RepositoryWebviewScreen
import com.example.githubuser.ui.userdetail.GithubUserDetailScreen
import com.example.githubuser.ui.userlist.GithubUserListScreen

@Composable
fun GithubUserNavHost(
    navHostController: NavHostController,
    startDestination: String = GithubUserDestination.USER_LIST_ROUTE,
    navAction: GithubUserNavigationActions = remember(navHostController) {
        GithubUserNavigationActions(navHostController)
    },
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(GithubUserDestination.USER_LIST_ROUTE) {
            GithubUserListScreen(onUserClick = { username ->
                navAction.navigateToUserDetail(username)
            })
        }

        composable(
            route = GithubUserDestination.USER_DETAIL_ROUTE,
            arguments = listOf(navArgument(USERNAME_ARG) { type = NavType.StringType })
        ) { navBackStackEntry ->
            val username = navBackStackEntry.arguments?.getString(USERNAME_ARG) ?: ""
            GithubUserDetailScreen(navHostController, username, onRepoClick = { repoDetail ->
                navAction.navigateToRepositoryDetail(repoDetail)
            })
        }

        composable(
            route = GithubUserDestination.REPO_DETAIL_ROUTE,
            arguments = listOf(
                navArgument(NAME_ARG) { type = NavType.StringType },
                navArgument(REPO_URL_ARG) { type = NavType.StringType },
            )
        ) { navBackStackEntry ->
            val name = navBackStackEntry.arguments?.getString(NAME_ARG) ?: ""
            val url = navBackStackEntry.arguments?.getString(REPO_URL_ARG) ?: ""
            RepositoryWebviewScreen(navHostController, url, name)
        }
    }

}