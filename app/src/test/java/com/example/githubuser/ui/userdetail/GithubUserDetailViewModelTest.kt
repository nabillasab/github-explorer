package com.example.githubuser.ui.userdetail

import androidx.lifecycle.SavedStateHandle
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.githubuser.data.Result
import com.example.githubuser.domain.GetRepoListUseCase
import com.example.githubuser.domain.GetUserDetailUseCase
import com.example.githubuser.ui.GithubUserDestinationArgs
import com.example.githubuser.helper.FakeModel
import com.example.githubuser.helper.MainDispatcherRule
import com.example.githubuser.helper.NoopListCallback
import com.example.githubuser.helper.RepositoryDiffCallback
import com.example.githubuser.ui.model.UiState
import com.example.githubuser.ui.model.User
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GithubUserDetailViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: GithubUserDetailViewModel
    private val getUserDetailUseCase: GetUserDetailUseCase = mockk()
    private val getRepoListUseCase: GetRepoListUseCase = mockk()
    private val username = "nabillasab"
    private lateinit var userDetail: User

    @Before
    fun setUp() {

    }

    @Test
    fun `username should be retrieved from SavedStateHandle`() {
        //Given
        val username = "bils"
        userDetail = FakeModel.getFakeUser()
        coEvery { getUserDetailUseCase.execute(username) } returns flow {
            emit(Result.Success(userDetail))
        }
        coEvery { getRepoListUseCase.execute(username) } returns flowOf(PagingData.empty())

        viewModel = GithubUserDetailViewModel(
            getUserDetailUseCase,
            getRepoListUseCase,
            SavedStateHandle(mapOf(GithubUserDestinationArgs.USERNAME_ARG to username))
        )

        val result = viewModel.username

        //when & then
        assertEquals(username, result)
    }

    @Test
    fun `username should be empty when it's not provided`() {
        //Given
        val username = ""
        userDetail = FakeModel.getFakeUser()
        coEvery { getUserDetailUseCase.execute("") } returns flow {
            emit(Result.Success(userDetail))
        }
        coEvery { getRepoListUseCase.execute("") } returns flowOf(PagingData.empty())

        viewModel = GithubUserDetailViewModel(
            getUserDetailUseCase,
            getRepoListUseCase,
            SavedStateHandle(mapOf(GithubUserDestinationArgs.USERNAME_ARG to null))
        )

        val result = viewModel.username

        //when & then
        assertEquals(username, result)
    }

    @Test
    fun `loadUserDetail emits Success UiState`() = runTest {
        //Given
        userDetail = FakeModel.getFakeUser()
        coEvery { getUserDetailUseCase.execute(username) } returns flow {
            emit(Result.Success(userDetail))
        }
        coEvery { getRepoListUseCase.execute(username) } returns flowOf(PagingData.empty())

        //this needs to be called after coEvery because we put the function in init { }
        viewModel = GithubUserDetailViewModel(
            getUserDetailUseCase,
            getRepoListUseCase,
            SavedStateHandle(mapOf(GithubUserDestinationArgs.USERNAME_ARG to username))
        )

        //When
        viewModel.loadUserDetail()

        //Assert
        viewModel.uiState.test {
            val item = awaitItem()
            assertTrue(item is UiState.Success)
            assertEquals(userDetail, (item as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadUserDetail emits Error UiState`() = runTest {
        //Given
        val errorMsg = "no internet connection"
        val flow = flowOf(Result.Error(errorMsg))

        coEvery { getUserDetailUseCase.execute(username) } returns flow
        coEvery { getRepoListUseCase.execute(username) } returns flowOf(PagingData.empty())

        //this needs to be called after coEvery because we put the function in init { }
        viewModel = GithubUserDetailViewModel(
            getUserDetailUseCase,
            getRepoListUseCase,
            SavedStateHandle(mapOf(GithubUserDestinationArgs.USERNAME_ARG to username))
        )

        //When
        viewModel.loadUserDetail()

        //Assert
        viewModel.uiState.test {
            val item = awaitItem()
            assertTrue(item is UiState.Error)
            assertEquals(errorMsg, (item as UiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadRepository emits expected paging data`() = runTest {
        //Given
        userDetail = FakeModel.getFakeUser()
        val pagingData = PagingData.from(FakeModel.getFakeRepoList())

        coEvery { getUserDetailUseCase.execute(username) } returns flow {
            emit(Result.Success(userDetail))
        }
        coEvery { getRepoListUseCase.execute(username) } returns flowOf(pagingData)

        //this needs to be called after coEvery because we put the function in init { }
        viewModel = GithubUserDetailViewModel(
            getUserDetailUseCase,
            getRepoListUseCase,
            SavedStateHandle(mapOf(GithubUserDestinationArgs.USERNAME_ARG to username))
        )

        //When
        val differ = AsyncPagingDataDiffer(
            diffCallback = RepositoryDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.repoPagingFlow.collectLatest {
                differ.submitData(it)
            }
        }

        advanceUntilIdle()

        //Then
        val snapshot = differ.snapshot()
        assertEquals(2, snapshot.size)
        assertEquals("github-user", snapshot[0]?.name)
        assertEquals("movie-project", snapshot[1]?.name)
        job.cancel()
    }

    @Test
    fun `loadUserDetail emits Loading then Success`() = runTest {
        //Given
        userDetail = FakeModel.getFakeUser()
        val flow = flow {
            emit(Result.Loading)
            delay(10)
            emit(Result.Success(userDetail))
        }

        coEvery { getUserDetailUseCase.execute(username) } returns flow
        coEvery { getRepoListUseCase.execute(username) } returns flowOf(PagingData.empty())

        //this needs to be called after coEvery because we put the function in init { }
        viewModel = GithubUserDetailViewModel(
            getUserDetailUseCase,
            getRepoListUseCase,
            SavedStateHandle(mapOf(GithubUserDestinationArgs.USERNAME_ARG to username))
        )

        viewModel.uiState.test {
            //When
            viewModel.loadUserDetail()

            //Assert
            assertEquals(UiState.Loading, awaitItem())

            val item = awaitItem()
            assertTrue(item is UiState.Success)
            assertEquals(userDetail, (item as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadUserDetail emits Loading then Error`() = runTest {
        //Given
        val errorMsg = "no internet connection"
        userDetail = FakeModel.getFakeUser()
        val flow = flow {
            emit(Result.Loading)
            delay(10)
            emit(Result.Error(errorMsg))
        }

        coEvery { getUserDetailUseCase.execute(username) } returns flow
        coEvery { getRepoListUseCase.execute(username) } returns flowOf(PagingData.empty())

        //this needs to be called after coEvery because we put the function in init { }
        viewModel = GithubUserDetailViewModel(
            getUserDetailUseCase,
            getRepoListUseCase,
            SavedStateHandle(mapOf(GithubUserDestinationArgs.USERNAME_ARG to username))
        )

        viewModel.uiState.test {
            //When
            viewModel.loadUserDetail()

            //Assert
            assertEquals(UiState.Loading, awaitItem())

            val item = awaitItem()
            assertTrue(item is UiState.Error)
            assertEquals(errorMsg, (item as UiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }
}