package com.example.githubuser.data.network

object AuthHelper {

    fun getDefaultHeader(): MutableMap<String, String> {
        val headers = mutableMapOf<String, String>()
        headers["accept"] = "application/vnd.github+json"
        headers["X-GitHub-Api-Version"] = "2022-11-28"
        return headers
    }
}