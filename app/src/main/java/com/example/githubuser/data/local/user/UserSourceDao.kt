package com.example.githubuser.data.local.user

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/**
 * this DAO is used for managing source main list / search
 */
@Dao
interface UserSourceDao {

    @Upsert
    suspend fun insertSources(sources: List<UserSourceEntity>)

    @Query("DELETE FROM user_source WHERE sourceType == 'MAIN_LIST'")
    suspend fun clearMainListSources()

    @Query("DELETE FROM user_source WHERE sourceType == 'SEARCH' AND searchQuery == :query")
    suspend fun clearSearchSources(query: String)

    @Query("SELECT COUNT(*) FROM user_source WHERE sourceType == 'MAIN_LIST'")
    suspend fun getMainListCount(): Int

    @Query(
        "SELECT COUNT(*) FROM user_source WHERE sourceType == 'SEARCH' AND searchQuery == :query"
    )
    suspend fun getSearchListCount(query: String): Int

    @Query("DELETE FROM user_source WHERE timestamp < :timestamp")
    suspend fun clearOldData(timestamp: Long)

    @Query(
        "DELETE FROM github_user WHERE lastUpdated < :timestamp AND id NOT IN (SELECT DISTINCT id FROM user_source)"
    )
    suspend fun clearStaleData(timestamp: Long)
}
