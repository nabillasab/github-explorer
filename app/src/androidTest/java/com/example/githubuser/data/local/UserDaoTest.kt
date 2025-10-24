package com.example.githubuser.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.githubuser.data.local.user.UserSourceEntity
import com.example.githubuser.helper.FakeModel
import com.example.githubuser.helper.FakeModel.toEntity
import com.example.githubuser.helper.ListPagingSource
import com.example.githubuser.helper.loadAll
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class UserDaoTest {

    private lateinit var db: GithubDatabase

    @Before
    fun initDb() {
        db = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            GithubDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertUsersMainListAndGetList() = runTest {
        // Given
        val userListLocalData = FakeModel.getFakeMainUserList().map { it.toEntity() }
        db.userDao().insertAll(userListLocalData)

        val userListSource = userListLocalData.map {
            UserSourceEntity(
                id = it.id,
                userId = it.userName,
                sourceType = "MAIN_LIST",
                searchQuery = "",
                position = it.id + 1,
                timestamp = it.lastUpdated
            )
        }
        db.userSourceDao().insertSources(userListSource)

        // When
        val expected = ListPagingSource(userListLocalData).loadAll()
        val loaded = db.userDao().getUsersResult().loadAll()

        // Then
        assertNotNull(loaded)
        assertEquals("mojombo", loaded[0].userName)
        assertEquals(expected, loaded)
    }

    @Test
    fun insertUsersSearchListAndGetList() = runTest {
        // Given
        val query = "nabillasab"
        val userListLocalData = FakeModel.getFakeSearchList().map { it.toEntity() }
        db.userDao().insertAll(userListLocalData)

        val userListSource = userListLocalData.map {
            UserSourceEntity(
                id = it.id,
                userId = it.userName,
                sourceType = "SEARCH",
                searchQuery = query,
                position = it.id + 1,
                timestamp = it.lastUpdated
            )
        }
        db.userSourceDao().insertSources(userListSource)

        // When
        val expected = ListPagingSource(userListLocalData).loadAll()
        val loaded = db.userDao().getSearchResult(query).loadAll()

        // Then
        assertNotNull(loaded)
        assertEquals("nabillasab", loaded[0].userName)
        assertEquals(loaded, expected)
    }

    @Test
    fun clearDbAndGetUsers() = runTest {
        val query = "nabillasab"
        val userListLocalData = FakeModel.getFakeUserList().map { it.toEntity() }
        db.userDao().insertAll(userListLocalData)

        db.userDao().clearAll()

        val loadedMain = db.userDao().getUsersResult().loadAll()
        val loadedSearch = db.userDao().getSearchResult(query).loadAll()

        assertEquals(true, loadedMain.isEmpty())
        assertEquals(true, loadedSearch.isEmpty())
    }

    @Test
    fun insertUsersAndGetFreshUserByUsername() = runTest {
        val username = "nabillasab"
        val userListLocalData = FakeModel.getFakeUserListContainsNull().map { it.toEntity() }
        db.userDao().insertAll(userListLocalData)

        val expected = ListPagingSource(userListLocalData).loadAll()[0]
        val loaded = db.userDao().getFreshUserByUsername(username)

        assertNotNull(loaded)
        assertEquals(loaded, expected)
        assertEquals(null, loaded?.fullName)
    }

    @Test
    fun insertUsersAndGetUserDataCompleted() = runTest {
        val username = "nabillasab"
        val userListLocalData = FakeModel.getFakeUserList().map { it.toEntity() }
        db.userDao().insertAll(userListLocalData)

        val expected = ListPagingSource(userListLocalData).loadAll()[0]
        val loaded = db.userDao().getUserDataCompleted(username)

        assertNotNull(loaded)
        assertEquals(loaded, expected)
        assertEquals("Nabilla Sabbaha", loaded?.fullName)
    }

    @Test
    fun insertUserFromSearchAndGetTheData() = runTest {
        // Given
        val query = "nabillasab"
        val userListLocalData = FakeModel.getFakeSearchList().map { it.toEntity() }
        db.userDao().insertAll(userListLocalData)

        val userListSource = userListLocalData.map {
            UserSourceEntity(
                id = it.id,
                userId = it.userName,
                sourceType = "SEARCH",
                searchQuery = query,
                position = it.id + 1,
                timestamp = it.lastUpdated
            )
        }
        db.userSourceDao().insertSources(userListSource)

        val userExpected = FakeModel.getFakeUser().toEntity()
        db.userDao().insert(userExpected)

        // When
        val expected = ListPagingSource(
            FakeModel
                .getFakeListGithubAfterInsert().map { it.toEntity() }
        ).loadAll()
        val loaded = db.userDao().getSearchResult(query).loadAll()

        // Then
        assertNotNull(loaded)
        assertEquals("nabillasab", loaded[0].userName)
        assertEquals("Nabilla Sabbaha", loaded[0].fullName)
        assertEquals("audrians", loaded[1].userName)
        assertEquals("", loaded[1].fullName)
    }

    @Test
    fun insertDbAndGetLastUpdated() = runTest {
        val userListLocalData = FakeModel.getFakeUserList().map { it.toEntity() }
        val expectedLastUpdated = userListLocalData[1].lastUpdated
        db.userDao().insertAll(userListLocalData)

        val loaded = db.userDao().getLastUpdated()

        assertEquals(expectedLastUpdated, loaded)
    }
}
