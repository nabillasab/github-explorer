package com.example.githubuser.data.local.repo

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface RemoteKeyRepoDao {
    @Query("SELECT * FROM REMOTEKEYREPOENTITY WHERE username == :username")
    suspend fun getRemoteKey(username: String): RemoteKeyRepoEntity?

    @Upsert
    suspend fun insertOrReplace(remoteKeys: RemoteKeyRepoEntity)

    @Query("DELETE FROM REMOTEKEYREPOENTITY WHERE username == :username")
    suspend fun clearKeyByUserName(username: String)
}