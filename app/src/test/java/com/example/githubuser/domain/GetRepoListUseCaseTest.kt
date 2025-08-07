package com.example.githubuser.domain

import androidx.paging.PagingData
import com.example.githubuser.helper.FakeModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GetRepoListUseCaseTest {

    private lateinit var useCase: GetRepoListUseCase
    private val repository: GithubUserRepository = mockk()

    @Before
    fun setup() {
        useCase = GetRepoListUseCase(repository)
    }

    @Test
    fun `use case should return paging data github repository from repository`() = runTest {
        //Given
        val username = "nabillasab"
        val repoPagingData = PagingData.from(FakeModel.getFakeRepoList())
        val flow = flowOf(repoPagingData)
        coEvery { repository.getRepoList(username) } returns flow

        //When
        val result = useCase.execute(username)

        //Then
        assertEquals(result, flow)
        coVerify(exactly = 1) { repository.getRepoList(username) }
    }
}