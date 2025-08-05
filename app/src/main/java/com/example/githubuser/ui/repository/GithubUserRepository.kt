package com.example.githubuser.ui.repository

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.githubuser.ui.components.SectionTitle
import com.example.githubuser.ui.components.WebviewScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryWebviewScreen(navController: NavController, url: String, titleRepo: String) {
    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back button"
                    )
                }
            }, title = { SectionTitle(titleRepo) })
        }) { paddingValues ->
        WebviewScreen(url, modifier = Modifier.padding(paddingValues))
    }
}