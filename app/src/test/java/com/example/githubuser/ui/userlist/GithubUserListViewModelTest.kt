package com.example.githubuser.ui.userlist

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.githubuser.domain.GithubUserRepository
import com.example.githubuser.helper.FakeModel
import com.example.githubuser.helper.MainDispatcherRule
import com.example.githubuser.helper.NoopListCallback
import com.example.githubuser.helper.UserDiffCallback
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GithubUserListViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: GithubUserListViewModel

    private val repository: GithubUserRepository = mockk()

    @Before
    fun setUp() {
        viewModel = GithubUserListViewModel(repository)
    }

    @Test
    fun `onSearchQueryChanged sets new value`() = runTest {
        // Given
        val expectedValue = "nabillasab"

        // When & Then
        viewModel.searchQuery.test {
            viewModel.onSearchQueryChanged(expectedValue)

            skipItems(1)

            assertEquals(expectedValue, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `userPagingFlow emits from repository getUserList when search query is empty`() = runTest {
        // Given
        val username = ""

        // expected to get main user list
        val userPagingData = PagingData.from(FakeModel.getFakeMainUserList())

        coEvery { repository.getUserList() } returns flowOf(userPagingData)
        coEvery { repository.getUserByUsername(username) } returns flowOf(PagingData.empty())

        // When
        viewModel.onSearchQueryChanged(username)
        val differ = AsyncPagingDataDiffer(
            diffCallback = UserDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.userPagingFlow.collectLatest {
                differ.submitData(it)
            }
        }

        advanceUntilIdle()

        val snapshot = differ.snapshot()
        assertEquals(2, snapshot.size)
        assertEquals("mojombo", snapshot[0]?.username)
        assertEquals("defunkt", snapshot[1]?.username)

        job.cancel()
    }

    @Test
    fun `userPagingFlow emits from repository search by username when search query is not empty`() =
        runTest {
            // Given
            val username = "nabillasab"
            val userPagingData = PagingData.from(FakeModel.getSearchUserList())

            coEvery { repository.getUserList() } returns flowOf(PagingData.empty())
            coEvery { repository.getUserByUsername(username) } returns flowOf(userPagingData)

            // When
            viewModel.onSearchQueryChanged(username)
            val differ = AsyncPagingDataDiffer(
                diffCallback = UserDiffCallback(),
                updateCallback = NoopListCallback(),
                workerDispatcher = Dispatchers.Main
            )

            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.userPagingFlow.collectLatest {
                    differ.submitData(it)
                }
            }

            advanceUntilIdle()

            val snapshot = differ.snapshot()
            assertEquals(2, snapshot.size)
            assertEquals("nabillasab", snapshot[0]?.username)
            assertEquals("audrians", snapshot[1]?.username)
            job.cancel()
        }
}
