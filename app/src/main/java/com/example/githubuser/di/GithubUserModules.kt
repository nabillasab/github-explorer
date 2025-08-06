package com.example.githubuser.di

import com.example.githubuser.data.GithubUserRepositoryImpl
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