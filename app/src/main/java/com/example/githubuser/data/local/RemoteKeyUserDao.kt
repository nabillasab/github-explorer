package com.example.githubuser.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyUserDao {
    @Query("SELECT * FROM RemoteKeyUserEntity ORDER BY id DESC LIMIT 1")
    suspend fun getLastRemoteKey(): RemoteKeyUserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeyUserEntity>)

    @Query("DELETE FROM RemoteKeyUserEntity")
    suspend fun clearKey()
}