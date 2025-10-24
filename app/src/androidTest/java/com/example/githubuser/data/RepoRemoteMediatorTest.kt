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
import com.example.githubuser.data.local.repo.RepositoryEntity
import com.example.githubuser.data.mediator.GithubRepoRemoteMediator
import com.example.githubuser.data.network.GithubApi
import com.example.githubuser.helper.FakeModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RepoRemoteMediatorTest {
    private lateinit var db: GithubDatabase
    private lateinit var repoRemoteMediator: GithubRepoRemoteMediator
    private val mockApi = mockk<GithubApi>()

    @Before
    fun initDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GithubDatabase::class.java
        ).allowMainThreadQueries().build()

        repoRemoteMediator = GithubRepoRemoteMediator(db, mockApi)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun refreshLoad_returnSuccessMainListAndSaveToDb() = runTest {
        // Given
        val username = "nabillasab"
        val repoListData = FakeModel.getFakeGithubRepoDataList()
        coEvery { mockApi.getRepositoryList(username, any(), any(), any()) } returns repoListData

        // When
        val pagingState = PagingState<Int, RepositoryEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        repoRemoteMediator.setUsername(username)
        val result = repoRemoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Success)

        // When
        val dbResult = db.repositoryDao().getRepoByUserName(username).load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        // Then
        Assert.assertTrue(dbResult is PagingSource.LoadResult.Page)
        Assert.assertEquals(2, (dbResult as PagingSource.LoadResult.Page).data.size)
        Assert.assertEquals("movie-project", dbResult.data[1].repoName)
    }
}
