package com.example.githubuser.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyRepoDao {
    @Query("SELECT * FROM REMOTEKEYREPOENTITY WHERE username == :username ORDER BY id DESC LIMIT 1")
    suspend fun getLastRemoteKey(username: String): RemoteKeyRepoEntity?

    @Query("SELECT * FROM REMOTEKEYREPOENTITY WHERE username == :username ORDER BY id DESC")
    suspend fun getCacheKey(username: String): List<RemoteKeyRepoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeyRepoEntity>)

    @Query("DELETE FROM REMOTEKEYREPOENTITY WHERE username == :username")
    suspend fun clearKeyByUserName(username: String)
}