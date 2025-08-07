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
class GetUserListUseCaseTest {

    private lateinit var useCase: GetUserListUseCase
    private val repository: GithubUserRepository = mockk()

    @Before
    fun setup() {
        useCase = GetUserListUseCase(repository)
    }

    @Test
    fun `use case should return paging data github user from repository`() = runTest {
        //Given
        val userPagingData = PagingData.from(FakeModel.getFakeUserList())
        val flow = flowOf(userPagingData)
        coEvery { repository.getUserList() } returns flow

        //When
        val result = useCase.execute()

        //Then
        assertEquals(result, flow)
        coVerify(exactly = 1) { repository.getUserList() }
    }

}