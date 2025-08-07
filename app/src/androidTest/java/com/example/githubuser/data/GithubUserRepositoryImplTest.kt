package com.example.githubuser.data

import android.content.Context
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.githubuser.data.local.GithubDatabase
import com.example.githubuser.data.local.UserDao
import com.example.githubuser.data.mediator.GithubRepoRemoteMediator
import com.example.githubuser.data.mediator.GithubUserRemoteMediator
import com.example.githubuser.data.network.GithubDataSource
import com.example.githubuser.data.network.GithubSearchUserPagingSource
import com.example.githubuser.domain.GithubUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GithubUserRepositoryImplTest {

//    private lateinit var repository: GithubUserRepository
//    private lateinit var db: GithubDatabase
//    private lateinit var userDao: UserDao
////    private val networkDataSource: GithubDataSource = mockk()
////    private val userRemoteMediator: GithubUserRemoteMediator = mockk()
////    private val searchUserFactory: GithubSearchUserPagingSource.Factory = mockk()
////    private val repoUserFactory: GithubRepoRemoteMediator.Factory = mockk()
//    private val dispatcher = StandardTestDispatcher()
//
//    @Before
//    fun setup() = runTest {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        db = Room.inMemoryDatabaseBuilder(
//            context,
//            GithubDatabase::class.java
//        ).allowMainThreadQueries().build()
//        userDao = db.userDao()
//
//        val userListLocalData = FakeModel.getFakeUserList().map { it.toEntity() }
//        userDao.insertAll(userListLocalData)
//
//        repository = GithubUserRepositoryImpl(
//            networkDataSource,
//            userRemoteMediator,
//            searchUserFactory,
//            repoUserFactory,
//            db,
//            dispatcher
//        )
//    }
//
//    @Test
//    fun `getUserList should return paging data user from paging source or db`() = runTest {
//
//        val pager = Pager(
//            config = PagingConfig(pageSize = 20),
//            pagingSourceFactory = { userDao.getUserPagingSource() }
//        )
//
//        val flow = pager.flow
//        val differ = AsyncPagingDataDiffer(
//            diffCallback = UserEntityDiffCallback(),
//            updateCallback = NoopListCallback(),
//            workerDispatcher = Dispatchers.Main
//        )
//
//        runTest {
//            val job = launch {
//                flow.collectLatest {
//                    differ.submitData(it)
//                }
//            }
//
//            advanceUntilIdle()
//
//            val items = differ.snapshot().items
//            assertEquals(2, items.size)
//            assertEquals("nabillasab", items[0].userName)
//            assertEquals("audrians", items[1].userName)
//
//            job.cancel()
//        }
//    }
//
//    @Test
//    fun `getUserList should return paging data user from remote mediator or network`() = runTest {
//
//        val result = repository.getUserList()
//
//    }
}