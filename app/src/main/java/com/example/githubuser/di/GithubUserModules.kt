package com.example.githubuser.di

import android.content.Context
import androidx.room.Room
import com.example.githubuser.data.GithubUserRepositoryImpl
import com.example.githubuser.data.local.GithubDatabase
import com.example.githubuser.data.local.repo.RemoteKeyRepoDao
import com.example.githubuser.data.local.user.RemoteKeyUserDao
import com.example.githubuser.data.local.repo.RepositoryDao
import com.example.githubuser.data.local.user.RemoteKeySearchDao
import com.example.githubuser.data.local.user.UserDao
import com.example.githubuser.data.local.user.UserSourceDao
import com.example.githubuser.data.network.GithubApi
import com.example.githubuser.data.network.GithubDataSource
import com.example.githubuser.data.network.GithubDataSourceImpl
import com.example.githubuser.data.network.NetworkExceptionInterceptor
import com.example.githubuser.domain.GithubUserRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GithubUserModules {

    @Singleton
    @Binds
    abstract fun bindGithubUserRepository(repository: GithubUserRepositoryImpl): GithubUserRepository

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(dataSource: GithubDataSourceImpl): GithubDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModules {

    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    fun provideNetworkExceptionInterceptor(): NetworkExceptionInterceptor {
        return NetworkExceptionInterceptor()
    }

    @Provides
    fun provideOkhttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkExceptionInterceptor: NetworkExceptionInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkExceptionInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideGithubApi(retrofit: Retrofit): GithubApi {
        return retrofit.create(GithubApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModules {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): GithubDatabase {
        return Room.databaseBuilder(
            context.applicationContext, GithubDatabase::class.java, "github_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: GithubDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideUserSourceDao(database: GithubDatabase): UserSourceDao {
        return database.userSourceDao()
    }

    @Provides
    fun provideRepositoryDao(database: GithubDatabase): RepositoryDao {
        return database.repositoryDao()
    }

    @Provides
    fun provideRemoteKeyUserDao(database: GithubDatabase): RemoteKeyUserDao {
        return database.remoteKeyUserDao()
    }

    @Provides
    fun provideRemoteKeySearchDao(database: GithubDatabase): RemoteKeySearchDao {
        return database.remoteKeySearchDao()
    }

    @Provides
    fun provideRemoteKeyRepoDao(database: GithubDatabase): RemoteKeyRepoDao {
        return database.remoteKeyRepoDao()
    }

}