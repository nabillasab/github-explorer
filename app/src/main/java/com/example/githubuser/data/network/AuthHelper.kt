package com.example.githubuser.data.network

import com.example.githubuser.BuildConfig

object AuthHelper {

    fun getDefaultHeader(): MutableMap<String, String> {
        val headers = mutableMapOf<String, String>()
        headers["accept"] = "application/vnd.github+json"
        headers["X-GitHub-Api-Version"] = "2022-11-28"
        headers["Authorization"] = "Bearer ${BuildConfig.API_KEY}"
        return headers
    }
}
