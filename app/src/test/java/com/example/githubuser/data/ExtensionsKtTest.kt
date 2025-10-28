package com.example.githubuser.data

import com.example.githubuser.data.local.repo.RepositoryEntity
import com.example.githubuser.data.local.user.UserEntity
import com.example.githubuser.ui.model.User
import com.example.githubuser.ui.userdetail.model.Repository
import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionsKtTest {

    @Test
    fun `GithubUserData toEntity should map all fields correctly`() {
        val githubUserData = getGithubUserData()
        val userEntity = getUserEntity()

        val result = githubUserData.toEntity()
        assertEquals(result, userEntity)
        assertEquals("Nabilla Sabbaha", result.fullName)
    }

    @Test
    fun `GithubUserData toEntity should map null values to null`() {
        val githubUserData = getGithubUserDataContainsNull()
        val userEntity = getUserEntityContainsNull()

        val result = githubUserData.toEntity()
        assertEquals(result, userEntity)
        assertEquals(null, result.avatarUrl)
        assertEquals(null, result.fullName)
        assertEquals(null, result.bio)
    }

    @Test
    fun `UserEntity toModel should map all fields correctly`() {
        val userEntity = getUserEntity()
        val user = getUser()

        val result = userEntity.toModel()
        assertEquals(result, user)
        assertEquals("Nabilla Sabbaha", result.fullName)
        assertEquals("halo", result.bio)
    }

    @Test
    fun `UserEntity toModel should map null values to null`() {
        val userEntity = getUserEntityContainsNull()
        val user = getUserContainsNull()

        val result = userEntity.toModel()
        assertEquals(result, user)
        assertEquals("nabillasab", result.username)
        assertEquals(null, result.avatarUrl)
        assertEquals(null, result.fullName)
        assertEquals(null, result.bio)
    }

    @Test
    fun `GithubUserData toModel() should map all fields correctly`() {
        val githubUserData = getGithubUserData()
        val user = getUser()

        val result = githubUserData.toModel()
        assertEquals(result, user)
        assertEquals("Nabilla Sabbaha", result.fullName)
        assertEquals("halo", result.bio)
    }

    @Test
    fun `GithubUserData toModel() should map null values to null`() {
        val githubUserData = getGithubUserDataContainsNull()
        val user = getUserContainsNull()

        val result = githubUserData.toModel()
        assertEquals(result, user)
        assertEquals(null, result.fullName)
        assertEquals(null, result.bio)
    }

    @Test
    fun `RepositoryEntity toModel() should map all fields correctly`() {
        val repositoryEntity = getRepositoryEntity()
        val repository = getRepository()

        val result = repositoryEntity.toModel()
        assertEquals(result, repository)
        assertEquals("movie-project", result.name)
        assertEquals("MIT License", result.licenseName)
    }

    @Test
    fun `RepositoryEntity toModel() map null values to null`() {
        val repositoryEntity = getRepositoryEntityContainsNull()
        val repository = getRepositoryContainsNull()

        val result = repositoryEntity.toModel()
        assertEquals(result, repository)
        assertEquals("movie-project", result.name)
        assertEquals(null, result.description)
        assertEquals(null, result.langRepo)
        assertEquals(null, result.licenseName)
    }

    @Test
    fun `GithubRepositoryData toEntity() should map all fields correctly`() {
        val repositoryData = getRepositoryData()
        val repositoryEntity = getRepositoryEntity()

        val result = repositoryData.toEntity("nabillasab")
        assertEquals(result, repositoryEntity)
        assertEquals("movie-project", result.repoName)
        assertEquals("MIT License", result.licenseName)
    }

    @Test
    fun `GithubRepositoryData toEntity() map null values to null`() {
        val repositoryData = getRepositoryDataContainsNull()
        val repositoryEntity = getRepositoryEntityContainsNull()

        val result = repositoryData.toEntity("nabillasab")
        assertEquals("movie-project", result.repoName)
        assertEquals(null, result.desc)
        assertEquals(null, result.language)
        assertEquals(null, result.licenseName)
    }

    private fun getRepositoryData(): GithubRepositoryData {
        return GithubRepositoryData(
            id = 2,
            repoName = "movie-project",
            desc = "this is movie mini project",
            language = "Kotlin",
            starCount = 0,
            repoUrl = "https://github.com/nabillasab/movieproject",
            isForkRepo = false,
            updatedAt = "2025-07-28T07:46:46Z",
            isPrivateRepo = false,
            forksCount = 3,
            license = GithubLisence("MIT License")
        )
    }

    private fun getRepositoryDataContainsNull(): GithubRepositoryData {
        return GithubRepositoryData(
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
    }

    private fun getRepositoryEntity(): RepositoryEntity {
        return RepositoryEntity(
            id = 2,
            userName = "nabillasab",
            repoName = "movie-project",
            desc = "this is movie mini project",
            language = "Kotlin",
            starCount = 0,
            repoUrl = "https://github.com/nabillasab/movieproject",
            isForkRepo = false,
            updatedAt = "2025-07-28T07:46:46Z",
            isPrivateRepo = false,
            forksCount = 3,
            licenseName = "MIT License"
        )
    }

    private fun getRepositoryEntityContainsNull(): RepositoryEntity {
        return RepositoryEntity(
            id = 2,
            userName = "nabillasab",
            repoName = "movie-project",
            desc = null,
            language = null,
            starCount = 0,
            repoUrl = "https://github.com/nabillasab/movieproject",
            isForkRepo = false,
            updatedAt = "2025-07-28T07:46:46Z",
            isPrivateRepo = false,
            forksCount = 3,
            licenseName = null
        )
    }

    private fun getRepositoryContainsNull(): Repository {
        return Repository(
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
    }

    private fun getRepository(): Repository {
        return Repository(
            id = 2,
            name = "movie-project",
            description = "this is movie mini project",
            langRepo = "Kotlin",
            star = 0,
            repoUrl = "https://github.com/nabillasab/movieproject",
            fork = false,
            updatedAt = "2025-07-28T07:46:46Z",
            private = false,
            forksCount = 3,
            licenseName = "MIT License"
        )
    }

    private fun getGithubUserData(): GithubUserData {
        return GithubUserData(
            id = 1,
            userName = "nabillasab",
            avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
            fullName = "Nabilla Sabbaha",
            followers = 1,
            following = 12,
            totalRepo = 11,
            bio = "halo"
        )
    }

    private fun getGithubUserDataContainsNull(): GithubUserData {
        return GithubUserData(
            id = 1,
            userName = "nabillasab",
            avatarUrl = null,
            fullName = null,
            followers = 1,
            following = 12,
            totalRepo = 11,
            bio = null
        )
    }

    private fun getUser(): User {
        return User(
            id = 1,
            username = "nabillasab",
            avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
            fullName = "Nabilla Sabbaha",
            followers = 1,
            following = 12,
            repoCount = 11,
            bio = "halo"
        )
    }

    private fun getUserContainsNull(): User {
        return User(
            id = 1,
            username = "nabillasab",
            avatarUrl = null,
            fullName = null,
            followers = 1,
            following = 12,
            repoCount = 11,
            bio = null
        )
    }

    private fun getUserEntityContainsNull(): UserEntity {
        return UserEntity(
            id = 1,
            userName = "nabillasab",
            avatarUrl = null,
            fullName = null,
            followers = 1,
            following = 12,
            totalRepo = 11,
            bio = null
        )
    }

    private fun getUserEntity(): UserEntity {
        return UserEntity(
            id = 1,
            userName = "nabillasab",
            avatarUrl = "https://avatars.githubusercontent.com/u/25047957?v=4",
            fullName = "Nabilla Sabbaha",
            followers = 1,
            following = 12,
            totalRepo = 11,
            bio = "halo"
        )
    }
}
