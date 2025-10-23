package com.example.githubuser.helper

import com.example.githubuser.data.GithubLisence
import com.example.githubuser.data.GithubRepositoryData
import com.example.githubuser.data.GithubUserData
import com.example.githubuser.data.local.repo.RepositoryEntity
import com.example.githubuser.data.local.UserEntity
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.userdetail.model.Repository

object FakeModel {

    fun getFakeRepoList(): List<Repository> {
        val repos = mutableListOf<Repository>()
        val repository3 = Repository(
            id = 1,
            name = "github-user",
            description = "Android Movie Playground!asdbajbdjabdjah ajkdbajhbda kjadkjabsdjkasbdjaksbdjasbdjkasbdjkasbd askjdbakjsbdkjasbd",
            langRepo = null,
            star = 6,
            repoUrl = "https://github.com/nabillasab/movieproject",
            fork = false,
            updatedAt = "2025-07-28T07:46:46Z",
            private = false,
            forksCount = 0,
            licenseName = "MIT License"
        )
        val repository4 = Repository(
            id = 2,
            name = "movie-project",
            description = null,
            langRepo = null,
            star = 0,
            repoUrl = "https://github.com/nabillasab/movieproject",
            fork = false,
            updatedAt = "2025-07-28T07:46:46Z",
            private = false,
            forksCount = 3,
            licenseName = null
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

    fun getFakeUserOldBio(): User {
        val user1 = User(
            id = 1,
            username = "nabillasab",
            avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
            fullName = "Nabilla Sabbaha",
            followers = 1,
            following = 12,
            repoCount = 11,
            bio = "old bio"
        )
        return user1
    }

    fun getFakeUserNewBio(): User {
        val user1 = User(
            id = 1,
            username = "nabillasab",
            avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
            fullName = "Nabilla Sabbaha",
            followers = 1,
            following = 12,
            repoCount = 11,
            bio = "new bio updated"
        )
        return user1
    }


    fun getFakeFreshUser(): User {
        val user1 = User(
            id = 1,
            username = "nabillasab",
            avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
            fullName = null,
            followers = 0,
            following = 0,
            repoCount = 0,
            bio = null
        )
        return user1
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

    fun User.toData(): GithubUserData {
        return GithubUserData(
            userName = this.username,
            id = this.id,
            avatarUrl = this.avatarUrl,
            fullName = this.fullName,
            totalRepo = this.repoCount,
            followers = this.followers,
            following = this.following,
            bio = this.bio
        )
    }

    fun Repository.toEntity(username: String): RepositoryEntity {
        return RepositoryEntity(
            id = this.id,
            userName = username,
            repoName = this.name,
            desc = this.description,
            language = this.langRepo,
            starCount = this.star,
            repoUrl = this.repoUrl,
            isForkRepo = this.fork,
            updatedAt = this.updatedAt,
            isPrivateRepo = this.private,
            forksCount = this.forksCount,
            licenseName = licenseName
        )
    }

    fun Repository.toData(): GithubRepositoryData {
        return GithubRepositoryData(
            id = this.id,
            repoName = this.name,
            desc = this.description,
            language = this.langRepo,
            starCount = this.star,
            repoUrl = this.repoUrl,
            isForkRepo = this.fork,
            updatedAt = this.updatedAt,
            isPrivateRepo = this.private,
            forksCount = this.forksCount,
            license = GithubLisence(this.licenseName)
        )
    }
}