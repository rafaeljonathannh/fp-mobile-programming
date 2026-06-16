package com.coffeebliss.data.repository

import com.coffeebliss.data.local.dao.MemberDao
import com.coffeebliss.data.local.dao.RedeemDao
import com.coffeebliss.data.local.dao.TransactionDao
import com.coffeebliss.data.local.entity.Member
import com.coffeebliss.data.local.entity.Redeem
import com.coffeebliss.data.local.entity.Transaction
import kotlinx.coroutines.flow.Flow

class CoffeeBlissRepository(
    private val memberDao: MemberDao,
    private val transactionDao: TransactionDao,
    private val redeemDao: RedeemDao
) {

    // ======================== MEMBER ========================

    suspend fun registerMember(member: Member): Long {
        return memberDao.insert(member)
    }

    suspend fun getMemberByEmail(email: String): Member? {
        return memberDao.getMemberByEmail(email)
    }

    fun getMemberById(id: Int): Flow<Member?> {
        return memberDao.getMemberById(id)
    }

    fun getAllMembers(): Flow<List<Member>> {
        return memberDao.getAllMembers()
    }

    suspend fun getMemberCount(): Int {
        return memberDao.getMemberCount()
    }

    suspend fun updateMemberStatus(memberId: Int, points: Int) {
        val status = when {
            points >= 500 -> "Platinum"
            points >= 200 -> "Gold"
            else -> "Silver"
        }
        memberDao.updateStatus(memberId, status)
    }

    // ======================== TRANSACTION ========================

    suspend fun recordTransaction(memberId: Int, amount: Long): Int {
        val points = (amount / 10000).toInt()
        val transaction = Transaction(
            memberId = memberId,
            amount = amount,
            pointsEarned = points
        )
        transactionDao.insert(transaction)
        memberDao.addPoints(memberId, points)
        return points
    }

    fun getTransactionsByMember(memberId: Int): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByMember(memberId)
    }

    // ======================== REDEEM ========================

    suspend fun redeemReward(memberId: Int, rewardName: String, pointsRequired: Int): Boolean {
        val redeem = Redeem(
            memberId = memberId,
            rewardName = rewardName,
            pointsUsed = pointsRequired
        )
        redeemDao.insert(redeem)
        memberDao.deductPoints(memberId, pointsRequired)
        return true
    }

    fun getRedeemsByMember(memberId: Int): Flow<List<Redeem>> {
        return redeemDao.getRedeemsByMember(memberId)
    }

    // ======================== UTILS ========================

    fun generateMemberNumber(count: Int): String {
        return "CB${String.format("%04d", count + 1)}"
    }

    fun calculatePoints(amount: Long): Int {
        return (amount / 10000).toInt()
    }
}
