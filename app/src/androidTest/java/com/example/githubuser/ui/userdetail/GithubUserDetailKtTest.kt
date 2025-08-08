package com.example.githubuser.ui.userdetail

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.githubuser.ui.theme.GithubUserTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GithubUserDetailKtTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun showListOfUsers() {
        rule.setContent {
            GithubUserTheme {

            }
        }
    }
}