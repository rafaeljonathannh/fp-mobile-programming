package com.coffeebliss.data.local.dao

import androidx.room.*
import com.coffeebliss.data.local.entity.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction): Long

    @Query("SELECT * FROM transactions WHERE memberId = :memberId ORDER BY transactionDate DESC")
    fun getTransactionsByMember(memberId: Int): Flow<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE memberId = :memberId")
    suspend fun getTotalSpendingByMember(memberId: Int): Long?

    @Query("SELECT * FROM transactions ORDER BY transactionDate DESC LIMIT 10")
    fun getRecentTransactions(): Flow<List<Transaction>>
}
