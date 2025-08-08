package com.example.githubuser.data

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import app.cash.turbine.test
import com.example.githubuser.data.fake.FakeRepoRemoteMediator
import com.example.githubuser.data.fake.FakeSearchUserPagingSource
import com.example.githubuser.data.local.RemoteKeyUserDao
import com.example.githubuser.data.local.RepositoryDao
import com.example.githubuser.data.local.UserDao
import com.example.githubuser.data.mediator.GithubRepoRemoteMediator
import com.example.githubuser.data.mediator.GithubUserRemoteMediator
import com.example.githubuser.data.network.GithubApi
import com.example.githubuser.data.network.GithubDataSource
import com.example.githubuser.data.network.GithubSearchUserPagingSource
import com.example.githubuser.domain.GithubUserRepository
import com.example.githubuser.helper.FakeModel
import com.example.githubuser.helper.FakeModel.toData
import com.example.githubuser.helper.FakeModel.toEntity
import com.example.githubuser.helper.ListPagingSource
import com.example.githubuser.helper.MainDispatcherRule
import com.example.githubuser.helper.NoopListCallback
import com.example.githubuser.helper.RepositoryDiffCallback
import com.example.githubuser.helper.UserDiffCallback
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GithubUserRepositoryImplTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var repository: GithubUserRepository
    private lateinit var networkDataSource: GithubDataSource
    private lateinit var userRemoteMediator: GithubUserRemoteMediator
    private lateinit var searchUserFactory: GithubSearchUserPagingSource.Factory
    private lateinit var repoUserFactory: GithubRepoRemoteMediator.Factory
    private lateinit var userDao: UserDao
    private lateinit var repositoryDao: RepositoryDao
    private lateinit var remoteKeyUserDao: RemoteKeyUserDao

    private lateinit var api: GithubApi

    @OptIn(ExperimentalPagingApi::class)
    @Before
    fun setUp() {
        api = mockk()
        userDao = mockk()
        networkDataSource = mockk()
        repositoryDao = mockk()
        remoteKeyUserDao = mockk()
        userRemoteMediator = mockk()

        searchUserFactory = object : GithubSearchUserPagingSource.Factory {
            override fun create(query: String): GithubSearchUserPagingSource =
                FakeSearchUserPagingSource(FakeModel.getFakeUserList().map { it.toData() })
        }
        repoUserFactory = object : GithubRepoRemoteMediator.Factory {
            override fun create(username: String): GithubRepoRemoteMediator =
                FakeRepoRemoteMediator()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getUserList should return paging data user from paging source`() = runTest {
        //Given
        val expected = FakeModel.getFakeUserList()
        val userEntityPagingData = ListPagingSource(expected.map { it.toEntity() })
        every { userDao.getUserPagingSource() } returns userEntityPagingData

        //When
        val differ = AsyncPagingDataDiffer(
            diffCallback = UserDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        //set mediator object to null
        repository = GithubUserRepositoryImpl(
            networkDataSource,
            null,
            searchUserFactory,
            repoUserFactory,
            userDao,
            repositoryDao,
            StandardTestDispatcher()
        )

        val job = launch(UnconfinedTestDispatcher()) {
            repository.getUserList().collectLatest {
                differ.submitData(it)
            }
        }

        advanceUntilIdle()

        //Then
        val snapshot = differ.snapshot()
        assertEquals(2, snapshot.size)
        assertEquals(expected, snapshot)
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getUserDetail should return flow result of user from paging source when user completed exist`() =
        runTest {
            val username = "nabillasab"
            val expected = FakeModel.getFakeUser()
            //suspend func we need to use coEvery
            coEvery { userDao.getFreshUserByUsername(username) } returns null
            coEvery { userDao.getUserDataCompleted(username) } returns expected.toEntity()

            //set mediator object to null
            repository = GithubUserRepositoryImpl(
                networkDataSource,
                null,
                searchUserFactory,
                repoUserFactory,
                userDao,
                repositoryDao,
                StandardTestDispatcher()
            )
            repository.getUserDetail(username).drop(1).test {
                val success = awaitItem()
                assertEquals(Result.Success(expected), success)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getUserDetail should return flow result of user from network when user completed is not exist`() =
        runTest {
            val username = "nabillasab"
            val freshUser = FakeModel.getFakeFreshUser()
            val expected = FakeModel.getFakeUser()
            //suspend func we need to use coEvery
            coEvery { networkDataSource.getUserDetail(username) } returns expected.toData()
            coEvery { userDao.getFreshUserByUsername(username) } returns freshUser.toEntity()
            coEvery { userDao.getUserDataCompleted(username) } returns null
            coEvery { userDao.insert(any()) } just Runs

            //set mediator object to null
            repository = GithubUserRepositoryImpl(
                networkDataSource,
                null,
                searchUserFactory,
                repoUserFactory,
                userDao,
                repositoryDao,
                StandardTestDispatcher()
            )
            repository.getUserDetail(username).drop(1).test {
                val success = awaitItem()
                assertEquals(Result.Success(expected), success)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getUserDetail should return flow error when something wrong`() = runTest {
        val username = "nabillasab"
        val errorMsg = "no internet connection"

        //suspend func we need to use coEvery
        coEvery { networkDataSource.getUserDetail(username) } throws Exception(errorMsg)
        coEvery { userDao.getFreshUserByUsername(username) } returns null
        coEvery { userDao.getUserDataCompleted(username) } returns null
        coEvery { userDao.insert(any()) } just Runs

        //set mediator object to null
        repository = GithubUserRepositoryImpl(
            networkDataSource,
            null,
            searchUserFactory,
            repoUserFactory,
            userDao,
            repositoryDao,
            StandardTestDispatcher()
        )
        repository.getUserDetail(username).drop(1).test {
            val error = awaitItem()
            assertTrue(error is Result.Error)
            assertEquals(errorMsg, (error as Result.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getRepoList should return paging data repository from paging source`() = runTest {
        //Given
        val username = "nabillasab"
        val expected = FakeModel.getFakeRepoList()
        val repoEntityPagingData = ListPagingSource(expected.map { it.toEntity(username) })
        every { repositoryDao.getRepoByUserName(username) } returns repoEntityPagingData

        //When
        val differ = AsyncPagingDataDiffer(
            diffCallback = RepositoryDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        //set mediator object to null
        repository = GithubUserRepositoryImpl(
            networkDataSource,
            null,
            searchUserFactory,
            repoUserFactory,
            userDao,
            repositoryDao,
            StandardTestDispatcher()
        )

        val job = launch(UnconfinedTestDispatcher()) {
            repository.getRepoList(username).collectLatest {
                differ.submitData(it)
            }
        }

        advanceUntilIdle()

        //Then
        val snapshot = differ.snapshot()
        assertEquals(2, snapshot.size)
        assertEquals(expected, snapshot)
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getUserByUsername should return paging data user from paging source`() = runTest {
        //Given
        val username = "nabillasab"
        val expected = FakeModel.getFakeUserList()

        //When
        val differ = AsyncPagingDataDiffer(
            diffCallback = UserDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        //set mediator object to null
        repository = GithubUserRepositoryImpl(
            networkDataSource,
            null,
            searchUserFactory,
            repoUserFactory,
            userDao,
            repositoryDao,
            StandardTestDispatcher()
        )

        val job = launch(UnconfinedTestDispatcher()) {
            repository.getUserByUsername(username).collectLatest {
                differ.submitData(it)
            }
        }

        advanceUntilIdle()

        //Then
        val snapshot = differ.snapshot()
        assertEquals(2, snapshot.size)
        assertEquals(expected, snapshot)
        job.cancel()
    }
}