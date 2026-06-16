package com.coffeebliss.data.local.dao

import androidx.room.*
import com.coffeebliss.data.local.entity.Redeem
import kotlinx.coroutines.flow.Flow

@Dao
interface RedeemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(redeem: Redeem): Long

    @Query("SELECT * FROM redeems WHERE memberId = :memberId ORDER BY redeemedAt DESC")
    fun getRedeemsByMember(memberId: Int): Flow<List<Redeem>>
}
