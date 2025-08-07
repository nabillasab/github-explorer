package com.example.githubuser.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM github_repo WHERE userName == :username ORDER BY id ASC")
    fun getRepoByUserName(username: String): PagingSource<Int, RepositoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<RepositoryEntity>)

    @Query("DELETE FROM github_repo WHERE userName == :username")
    suspend fun clearDataByUsername(username: String)

    @Query("SELECT MAX(lastUpdated) FROM github_repo")
    suspend fun getLastUpdated(): Long?
}