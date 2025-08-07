package com.example.githubuser.domain

import androidx.paging.PagingData
import com.example.githubuser.helper.FakeModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchUserUseCaseTest {

    private lateinit var useCase: SearchUserUseCase
    private val repository: GithubUserRepository = mockk()

    @Before
    fun setup() {
        useCase = SearchUserUseCase(repository)
    }

    @Test
    fun `use case should return paging data github user from repository`() = runTest {
        //Given
        val username = "nabillasab"
        val userPagingData = PagingData.from(FakeModel.getFakeUserList())
        val flow = flowOf(userPagingData)
        coEvery { repository.getUserByUsername(username) } returns flow

        //When
        val result = useCase.execute(username)

        //Then
        assertEquals(result, flow)
        coVerify(exactly = 1) { repository.getUserByUsername(username) }
    }

}