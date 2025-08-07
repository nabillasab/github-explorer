package com.example.githubuser.domain

import com.example.githubuser.data.Result
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
class GetUserDetailUseCaseTest {

    private lateinit var useCase: GetUserDetailUseCase
    private val repository: GithubUserRepository = mockk()

    @Before
    fun setup() {
        useCase = GetUserDetailUseCase(repository)
    }

    @Test
    fun `use case should return user detail from repository`() = runTest {
        //Given
        val username = "nabillasab"
        val userData = FakeModel.getFakeUser()
        val flow = flowOf(Result.Success(userData))
        coEvery { repository.getUserDetail(username) } returns flow

        //When
        val result = useCase.execute(username)

        //Then
        assertEquals(result, flow)
        coVerify(exactly = 1) { repository.getUserDetail(username) }
    }
}