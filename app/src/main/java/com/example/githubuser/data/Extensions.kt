package com.example.githubuser.data

import com.example.githubuser.data.local.repo.RepositoryEntity
import com.example.githubuser.data.local.user.UserEntity
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.userdetail.model.Repository

fun GithubUserData.toEntity(): UserEntity {
    return UserEntity(
        userName = this.userName,
        id = this.id,
        avatarUrl = this.avatarUrl,
        fullName = this.fullName,
        totalRepo = this.totalRepo,
        followers = this.followers,
        following = this.following,
        bio = this.bio,
        lastUpdated = System.currentTimeMillis()
    )
}

fun UserEntity.toModel(): User {
    return User(
        id = this.id,
        username = this.userName,
        avatarUrl = this.avatarUrl,
        fullName = this.fullName,
        repoCount = this.totalRepo,
        followers = this.followers,
        following = this.following,
        bio = this.bio
    )
}

fun GithubUserData.toModel(): User {
    return User(
        id = this.id,
        username = this.userName,
        avatarUrl = this.avatarUrl,
        fullName = this.fullName,
        repoCount = this.totalRepo,
        followers = this.followers,
        following = this.following,
        bio = this.bio
    )
}

fun RepositoryEntity.toModel(): Repository {
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
        licenseName = this.licenseName
    )
}

fun GithubRepositoryData.toEntity(username: String): RepositoryEntity {
    return RepositoryEntity(
        id = this.id,
        userName = username,
        repoName = this.repoName,
        desc = this.desc,
        language = this.language,
        starCount = this.starCount,
        repoUrl = this.repoUrl,
        isForkRepo = this.isForkRepo,
        updatedAt = this.updatedAt,
        isPrivateRepo = this.isPrivateRepo,
        forksCount = this.forksCount,
        licenseName = license?.name
    )
}
