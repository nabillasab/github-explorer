package com.example.githubuser.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM github_user ORDER BY id ASC")
    fun getUserPagingSource(): PagingSource<Int, UserEntity>

    @Query("SELECT * FROM github_user ORDER BY id DESC LIMIT 1")
    fun getLastUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT * FROM github_user WHERE userName == :username AND fullName IS NULL")
    suspend fun getFreshUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM github_user WHERE userName == :username AND fullName IS NOT NULL")
    suspend fun getUserDataCompleted(username: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("DELETE FROM github_user")
    suspend fun clearAll()

    @Query("SELECT MAX(lastUpdated) FROM github_user")
    suspend fun getLastUpdated(): Long?
}