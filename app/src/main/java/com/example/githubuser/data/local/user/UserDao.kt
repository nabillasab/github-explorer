package com.example.githubuser.data.local.user

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserDao {

    @Query(
        """SELECT DISTINCT u.* FROM github_user u
            INNER JOIN user_source us ON u.id == us.id
             WHERE us.sourceType == "MAIN_LIST"
             ORDER BY us.position ASC 
        """
    )
    fun getUsersResult(): PagingSource<Int, UserEntity>

    @Query(
        """SELECT DISTINCT u.* FROM github_user u
            INNER JOIN user_source us ON u.id == us.id
             WHERE us.sourceType == "SEARCH" and us.searchQuery == :query
             ORDER BY us.position ASC 
        """
    )
    fun getSearchResult(query: String): PagingSource<Int, UserEntity>

    @Upsert
    suspend fun insertAll(users: List<UserEntity>)

    @Query("DELETE FROM github_user")
    suspend fun clearAll()

    @Query("SELECT * FROM github_user WHERE userName == :username AND fullName IS NULL")
    suspend fun getFreshUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM github_user WHERE userName == :username AND fullName IS NOT NULL")
    suspend fun getUserDataCompleted(username: String): UserEntity?

    @Upsert
    suspend fun insert(user: UserEntity)

    @Query("SELECT MAX(lastUpdated) FROM github_user")
    suspend fun getLastUpdated(): Long?
}
