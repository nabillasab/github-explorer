package com.example.githubuser.data.local.repo

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM github_repo WHERE userName == :username ORDER BY id ASC")
    fun getRepoByUserName(username: String): PagingSource<Int, RepositoryEntity>

    @Upsert
    suspend fun insertAll(repos: List<RepositoryEntity>)

    @Query("DELETE FROM github_repo WHERE userName == :username")
    suspend fun clearDataByUsername(username: String)

    @Query("SELECT MAX(lastUpdated) FROM github_repo")
    suspend fun getLastUpdated(): Long?
}
