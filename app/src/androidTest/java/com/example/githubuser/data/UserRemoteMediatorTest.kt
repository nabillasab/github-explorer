package com.example.githubuser.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.githubuser.data.local.GithubDatabase
import com.example.githubuser.data.local.user.UserEntity
import com.example.githubuser.data.mediator.GithubUserRemoteMediator
import com.example.githubuser.data.network.GithubApi
import com.example.githubuser.helper.FakeModel
import com.example.githubuser.helper.FakeModel.toUserData
import com.google.gson.JsonSyntaxException
import io.mockk.coEvery
import io.mockk.mockk
import java.net.SocketTimeoutException
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class UserRemoteMediatorTest {
    private lateinit var db: GithubDatabase
    private lateinit var userRemoteMediator: GithubUserRemoteMediator
    private val mockApi = mockk<GithubApi>()

    @Before
    fun initDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GithubDatabase::class.java
        ).allowMainThreadQueries().build()

        userRemoteMediator = GithubUserRemoteMediator(db, mockApi)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun refreshLoad_returnSuccessMainListAndSaveToDb() = runTest {
        // Given
        val userListentity = FakeModel.getFakeMainUserList().map { it.toUserData() }
        coEvery { mockApi.getUserList(any(), any(), any()) } returns userListentity

        // When
        val pagingState = PagingState<Int, UserEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = userRemoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Success)

        // When
        val dbResult = db.userDao().getUsersResult().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        // Then
        Assert.assertTrue(dbResult is PagingSource.LoadResult.Page)
        Assert.assertEquals(2, (dbResult as PagingSource.LoadResult.Page).data.size)
        Assert.assertEquals("mojombo", dbResult.data[0].userName)
    }

    @Test
    fun loadReturnWhenApiReturnsHttpError() = runTest {
        // Given
        val exception = HttpException(
            Response.error<Any>(404, "Not Found".toResponseBody())
        )
        coEvery { mockApi.getUserList(any(), any(), any()) } throws exception

        // When
        val pagingState = PagingState<Int, UserEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )
        val result = userRemoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Error)
        val error = result as RemoteMediator.MediatorResult.Error
        Assert.assertTrue(error.throwable is HttpException)
    }

    @Test
    fun loadReturnWhenApiReturnsTimeout() = runTest {
        // Given
        val exception = SocketTimeoutException("socket timed out")
        coEvery { mockApi.getUserList(any(), any(), any()) } throws exception

        // When
        val pagingState = PagingState<Int, UserEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )
        val result = userRemoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Error)
        // no assert for the object because SocketTimeoutException is caught as UndeclaredThrowableException
    }

    @Test
    fun loadReturnWhenJsonParsingFails() = runTest {
        // Given
        val exception = JsonSyntaxException("Malformed JSON")
        coEvery { mockApi.getUserList(any(), any(), any()) } throws exception

        // When
        val pagingState = PagingState<Int, UserEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )
        val result = userRemoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Error)
        val error = result as RemoteMediator.MediatorResult.Error
        Assert.assertTrue(error.throwable is JsonSyntaxException)
    }
}
