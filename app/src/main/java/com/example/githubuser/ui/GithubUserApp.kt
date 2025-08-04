package com.example.githubuser.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.githubuser.ui.theme.GithubUserTheme

@Composable
fun GithubUserApp() {
    GithubUserTheme {
        val navController = rememberNavController()

        GithubUserNavHost(
            navHostController = navController,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}