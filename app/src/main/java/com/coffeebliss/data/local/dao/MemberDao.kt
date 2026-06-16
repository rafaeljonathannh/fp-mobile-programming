package com.coffeebliss.data.local.dao

import androidx.room.*
import com.coffeebliss.data.local.entity.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(member: Member): Long

    @Update
    suspend fun update(member: Member)

    @Query("SELECT * FROM members WHERE id = :id")
    fun getMemberById(id: Int): Flow<Member?>

    @Query("SELECT * FROM members WHERE email = :email LIMIT 1")
    suspend fun getMemberByEmail(email: String): Member?

    @Query("SELECT * FROM members ORDER BY registeredAt DESC")
    fun getAllMembers(): Flow<List<Member>>

    @Query("UPDATE members SET totalPoints = totalPoints + :points WHERE id = :memberId")
    suspend fun addPoints(memberId: Int, points: Int)

    @Query("UPDATE members SET totalPoints = totalPoints - :points WHERE id = :memberId")
    suspend fun deductPoints(memberId: Int, points: Int)

    @Query("UPDATE members SET status = :status WHERE id = :memberId")
    suspend fun updateStatus(memberId: Int, status: String)

    @Query("SELECT COUNT(*) FROM members")
    suspend fun getMemberCount(): Int
}
