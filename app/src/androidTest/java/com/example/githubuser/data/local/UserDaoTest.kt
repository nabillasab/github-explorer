package com.example.githubuser.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.githubuser.helper.FakeModel
import com.example.githubuser.helper.FakeModel.toEntity
import com.example.githubuser.helper.ListPagingSource
import com.example.githubuser.helper.loadAll
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
            getApplicationContext(), GithubDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Test
    fun insertUsersAndGetListOfUsers() = runTest {
        val userListLocalData = FakeModel.getFakeUserList().map { it.toEntity() }
        db.userDao().insertAll(userListLocalData)

        val expected = ListPagingSource(userListLocalData).loadAll()
        val loaded = db.userDao().getUserPagingSource().loadAll()

        assertNotNull(loaded)
        assertEquals(loaded, expected)
    }

    @Test
    fun insertUsersAndGetLastUser() = runTest {
        val userListLocalData = FakeModel.getFakeUserList().map { it.toEntity() }
        db.userDao().insertAll(userListLocalData)

        val expected = ListPagingSource(userListLocalData).loadAll()[1]
        val loaded = db.userDao().getLastUser()

        assertNotNull(loaded)
        assertEquals(loaded, expected)
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
    fun insertUserAndGetLastUserInserted() = runTest {
        val userListLocalData = FakeModel.getFakeUserList().map { it.toEntity() }
        db.userDao().insert(userListLocalData[0])

        val expected = ListPagingSource(userListLocalData).loadAll()[0]
        val loaded = db.userDao().getLastUser()

        assertNotNull(loaded)
        assertEquals(loaded, expected)
        assertEquals("Nabilla Sabbaha", loaded?.fullName)
    }

    @Test
    fun clearDbAndGetUsers() = runTest {
        val userListLocalData = FakeModel.getFakeUserList().map { it.toEntity() }
        db.userDao().insertAll(userListLocalData)

        db.userDao().clearAll()

        val loaded = db.userDao().getUserPagingSource().loadAll()
        assertEquals(true, loaded.isEmpty())
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

