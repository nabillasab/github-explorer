package com.example.githubuser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UserEntity::class, RepositoryEntity::class,
        RemoteKeyUserEntity::class, RemoteKeyRepoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun repositoryDao(): RepositoryDao
    abstract fun remoteKeyUserDao(): RemoteKeyUserDao
    abstract fun remoteKeyRepoDao(): RemoteKeyRepoDao
}