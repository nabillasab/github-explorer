package com.example.githubuser.data.local.user

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/**
 * this class is used for managing pagination
 */
@Dao
interface RemoteKeySearchDao {

    @Upsert
    suspend fun insertOrReplace(remoteKeys: RemoteKeySearchEntity)

    @Query("SELECT * FROM search_remote_key WHERE searchQuery == :query")
    suspend fun getRemoteKey(query: String): RemoteKeySearchEntity?

    @Query("DELETE FROM search_remote_key WHERE searchQuery == :query")
    suspend fun clearKey(query: String)
}
