package com.example.githubuser.data.local.user

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/**
 * this class is used for managing pagination
 */
@Dao
interface RemoteKeyUserDao {

    @Upsert
    suspend fun insertOrReplace(remoteKeys: RemoteKeyUserEntity)

    @Query("SELECT * FROM user_remote_key WHERE id == 'user_list'")
    suspend fun getRemoteKey(): RemoteKeyUserEntity?

    @Query("DELETE FROM user_remote_key")
    suspend fun clearKey()
}
