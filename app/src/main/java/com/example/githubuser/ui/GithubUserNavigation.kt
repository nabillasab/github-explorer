package com.example.githubuser.ui

import android.net.Uri
import androidx.navigation.NavHostController
import com.example.githubuser.ui.GithubUserDestinationArgs.NAME_ARG
import com.example.githubuser.ui.GithubUserDestinationArgs.REPO_URL_ARG
import com.example.githubuser.ui.GithubUserDestinationArgs.USERNAME_ARG
import com.example.githubuser.ui.GithubUserScreen.REPO_DETAIL_SCREEN
import com.example.githubuser.ui.GithubUserScreen.USER_DETAIL_SCREEN
import com.example.githubuser.ui.GithubUserScreen.USER_LIST_SCREEN

/**
 * Arguments used in
 */
object GithubUserDestinationArgs {
    const val USERNAME_ARG = "username"
    const val NAME_ARG = "name"
    const val REPO_URL_ARG = "encodedUrl"
}

private object GithubUserScreen {
    const val USER_LIST_SCREEN = "userList"
    const val USER_DETAIL_SCREEN = "userDetail"
    const val REPO_DETAIL_SCREEN = "repositoryDetail"
}

object GithubUserDestination {
    const val USER_LIST_ROUTE = USER_LIST_SCREEN
    const val USER_DETAIL_ROUTE = "$USER_DETAIL_SCREEN/{$USERNAME_ARG}"
    const val REPO_DETAIL_ROUTE = "$REPO_DETAIL_SCREEN/{$NAME_ARG}/{$REPO_URL_ARG}"
}

class GithubUserNavigationActions(private val navController: NavHostController) {

    fun navigateToUserDetail(username: String) {
        navController.navigate("$USER_DETAIL_SCREEN/$username")
    }
    fun navigateToRepositoryDetail(repoDetail: Pair<String, String>) {
        val name = Uri.encode(repoDetail.first)
        val encodedUrl = Uri.encode(repoDetail.second)
        navController.navigate("$REPO_DETAIL_SCREEN/$name/$encodedUrl")
    }
}
