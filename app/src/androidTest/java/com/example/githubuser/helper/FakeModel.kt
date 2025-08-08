package com.example.githubuser.helper

import com.example.githubuser.data.GithubLisence
import com.example.githubuser.data.GithubRepositoryData
import com.example.githubuser.data.GithubUserData
import com.example.githubuser.data.local.UserEntity
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.userdetail.model.Repository

object FakeModel {

    fun getFakeGithubRepoDataList(): List<GithubRepositoryData> {
        val repos = mutableListOf<GithubRepositoryData>()
        val repository3 = GithubRepositoryData(
            id = 1,
            repoName = "github-user",
            desc = "Android Movie Playground!asdbajbdjabdjah ajkdbajhbda kjadkjabsdjkasbdjaksbdjasbdjkasbdjkasbd askjdbakjsbdkjasbd",
            language = null,
            starCount = 6,
            repoUrl = "https://github.com/nabillasab/movieproject",
            isForkRepo = false,
            updatedAt = "2025-07-28T07:46:46Z",
            isPrivateRepo = false,
            forksCount = 0,
            license = GithubLisence("MIT License")
        )
        val repository4 = GithubRepositoryData(
            id = 2,
            repoName = "movie-project",
            desc = null,
            language = null,
            starCount = 0,
            repoUrl = "https://github.com/nabillasab/movieproject",
            isForkRepo = false,
            updatedAt = "2025-07-28T07:46:46Z",
            isPrivateRepo = false,
            forksCount = 3,
            license = null
        )
        repos.add(repository3)
        repos.add(repository4)
        return repos
    }

    fun getFakeUser(): User {
        val user1 = User(
            id = 1,
            username = "nabillasab",
            avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
            fullName = "Nabilla Sabbaha",
            followers = 1,
            following = 12,
            repoCount = 11,
            bio = null
        )
        return user1
    }

    fun getFakeListGithubUserData(): List<GithubUserData> {
        val githubUserDataList = mutableListOf<GithubUserData>()
        val user1 = GithubUserData(
            id = 1,
            userName = "nabillasab",
            avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
            fullName = "Nabilla Sabbaha",
            followers = 1,
            following = 12,
            totalRepo = 11,
            bio = null
        )
        val user2 = GithubUserData(
            id = 2,
            userName = "audrians",
            avatarUrl = "https://avatars.githubusercontent.com/u/6389222?v=4",
            fullName = "Audria Nabilla",
            followers = 4,
            following = 22,
            totalRepo = 12,
            bio = null
        )
        githubUserDataList.add(user1)
        githubUserDataList.add(user2)
        return githubUserDataList
    }

    fun getFakeUserList(): List<User> {
        val userList = mutableListOf<User>()
        val user1 = User(
            id = 1,
            username = "nabillasab",
            avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
            fullName = "Nabilla Sabbaha",
            followers = 1,
            following = 12,
            repoCount = 11,
            bio = null
        )
        val user2 = User(
            id = 2,
            username = "audrians",
            avatarUrl = "https://avatars.githubusercontent.com/u/6389222?v=4",
            fullName = "Audria Nabilla",
            followers = 4,
            following = 22,
            repoCount = 12,
            bio = null
        )
        userList.add(user1)
        userList.add(user2)
        return userList
    }

    fun getFakeUserListContainsNull(): List<User> {
        val userList = mutableListOf<User>()
        val user1 = User(
            id = 1,
            username = "nabillasab",
            avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
            fullName = null,
            followers = 1,
            following = 12,
            repoCount = 11,
            bio = null
        )
        val user2 = User(
            id = 2,
            username = "audrians",
            avatarUrl = "https://avatars.githubusercontent.com/u/6389222?v=4",
            fullName = null,
            followers = 4,
            following = 22,
            repoCount = 12,
            bio = null
        )
        userList.add(user1)
        userList.add(user2)
        return userList
    }

    fun User.toEntity(): UserEntity {
        return UserEntity(
            userName = this.username,
            id = this.id,
            avatarUrl = this.avatarUrl,
            fullName = this.fullName,
            totalRepo = this.repoCount,
            followers = this.followers,
            following = this.following,
            bio = this.bio,
            lastUpdated = System.currentTimeMillis()
        )
    }

    fun GithubRepositoryData.toModel(): Repository {
        return Repository(
            id = this.id,
            name = this.repoName,
            description = this.desc,
            langRepo = this.language,
            star = this.starCount,
            repoUrl = this.repoUrl,
            fork = this.isForkRepo,
            updatedAt = this.updatedAt,
            private = this.isPrivateRepo,
            forksCount = this.forksCount,
            licenseName = license?.name
        )
    }
}