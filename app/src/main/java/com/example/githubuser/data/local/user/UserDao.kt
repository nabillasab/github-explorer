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

//    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
//    suspend fun insertUser(user: UserEntity)

//    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
//    fun getUserById(id: Int): UserEntity?

    @Query("DELETE FROM github_user")
    suspend fun clearAll()

    // REVIEW THIS BELOW CODE LATER =============

//    @Query("SELECT * FROM github_user ORDER BY id ASC")
//    fun getUserPagingSource(): PagingSource<Int, UserEntity>

//    @Query("SELECT * FROM github_user WHERE userName LIKE '%' || :query || '%' ORDER BY id ASC")
//    fun searchUsers(query: String): PagingSource<Int, UserEntity>

    @Query("SELECT * FROM github_user ORDER BY id DESC LIMIT 1")
    fun getLastUser(): UserEntity?

    @Query("SELECT * FROM github_user WHERE userName == :username AND fullName IS NULL")
    suspend fun getFreshUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM github_user WHERE userName == :username AND fullName IS NOT NULL")
    suspend fun getUserDataCompleted(username: String): UserEntity?

    @Upsert
    suspend fun insert(user: UserEntity)

    @Query("SELECT MAX(lastUpdated) FROM github_user")
    suspend fun getLastUpdated(): Long?
}