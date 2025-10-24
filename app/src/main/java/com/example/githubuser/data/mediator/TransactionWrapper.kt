package com.example.githubuser.data.mediator

import androidx.room.withTransaction
import com.example.githubuser.data.local.GithubDatabase
import javax.inject.Inject

@Deprecated("remove this")
interface TransactionRunner {
    suspend fun <T> runInTransaction(block: suspend () -> T): T
}

open class RoomTransactionRunner @Inject constructor(
    private val db: GithubDatabase
) : TransactionRunner {
    override suspend fun <T> runInTransaction(block: suspend () -> T): T =
        db.withTransaction { block() }
}
