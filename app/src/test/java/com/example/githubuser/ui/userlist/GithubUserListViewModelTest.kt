package com.example.githubuser.ui.userlist

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.githubuser.domain.GetUserListUseCase
import com.example.githubuser.domain.SearchUserUseCase
import com.example.githubuser.helper.FakeModel
import com.example.githubuser.helper.MainDispatcherRule
import com.example.githubuser.helper.NoopListCallback
import com.example.githubuser.helper.UserDiffCallback
import io.mockk.coEvery
import io.mockk.coVerify
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
    private val getUserListUseCase: GetUserListUseCase = mockk()
    private val searchUserUseCase: SearchUserUseCase = mockk()

    @Before
    fun setUp() {
        viewModel = GithubUserListViewModel(getUserListUseCase, searchUserUseCase)
    }

    @Test
    fun `onSearchQueryChanged sets new value`() = runTest {
        //Given
        val expectedValue = "nabillasab"

        //When & Then
        viewModel.searchQuery.test {
            viewModel.onSearchQueryChanged(expectedValue)

            skipItems(1)

            assertEquals(expectedValue, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `userPagingFlow emits from getUserListUseCase when search query is empty`() = runTest {
        //Given
        val username = ""

        val userPagingData = PagingData.from(FakeModel.getFakeUserList())

        coEvery { getUserListUseCase.execute() } returns flowOf(userPagingData)
        coEvery { searchUserUseCase.execute(username) } returns flowOf(PagingData.empty())

        //When
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

        //Then
        //validating this function was called at least once during the test execution
        coVerify { getUserListUseCase.execute() }
        //validating the searchUserUseCase.execute() function was never called
        coVerify(exactly = 0) { searchUserUseCase.execute(username) }

        val snapshot = differ.snapshot()
        assertEquals(2, snapshot.size)
        assertEquals("nabillasab", snapshot[0]?.username)
        assertEquals("audrians", snapshot[1]?.username)

        job.cancel()
    }

    @Test
    fun `userPagingFlow emits from searchUserUseCase when search query is not empty`() = runTest {
        //Given
        val username = "nabillasab"
        val userPagingData = PagingData.from(FakeModel.getFakeUserList())

        coEvery { getUserListUseCase.execute() } returns flowOf(PagingData.empty())
        coEvery { searchUserUseCase.execute(username) } returns flowOf(userPagingData)

        //When
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

        //Then
        //validating this function was called at least once during the test execution
        coVerify { searchUserUseCase.execute(username) }
        //validating the getUserListUseCase.execute() function was never called
        coVerify(exactly = 0) { getUserListUseCase.execute() }

        val snapshot = differ.snapshot()
        assertEquals(2, snapshot.size)
        assertEquals("nabillasab", snapshot[0]?.username)
        assertEquals("audrians", snapshot[1]?.username)
        job.cancel()
    }
}