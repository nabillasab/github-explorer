package com.example.githubuser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.githubuser.data.local.repo.RemoteKeyRepoDao
import com.example.githubuser.data.local.repo.RemoteKeyRepoEntity
import com.example.githubuser.data.local.repo.RepositoryDao
import com.example.githubuser.data.local.repo.RepositoryEntity
import com.example.githubuser.data.local.user.RemoteKeySearchDao
import com.example.githubuser.data.local.user.RemoteKeySearchEntity
import com.example.githubuser.data.local.user.RemoteKeyUserDao
import com.example.githubuser.data.local.user.RemoteKeyUserEntity
import com.example.githubuser.data.local.user.UserDao
import com.example.githubuser.data.local.user.UserEntity
import com.example.githubuser.data.local.user.UserSourceDao
import com.example.githubuser.data.local.user.UserSourceEntity

@Database(
    entities = [
        UserEntity::class,
        UserSourceEntity::class,
        RepositoryEntity::class,
        RemoteKeyUserEntity::class,
        RemoteKeySearchEntity::class,
        RemoteKeyRepoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun userSourceDao(): UserSourceDao
    abstract fun repositoryDao(): RepositoryDao
    abstract fun remoteKeyUserDao(): RemoteKeyUserDao
    abstract fun remoteKeySearchDao(): RemoteKeySearchDao
    abstract fun remoteKeyRepoDao(): RemoteKeyRepoDao
}
