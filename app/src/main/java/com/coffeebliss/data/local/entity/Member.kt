package com.coffeebliss.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val memberNumber: String,
    val totalPoints: Int = 0,
    val status: String = "Silver", // Silver, Gold, Platinum
    val registeredAt: Long = System.currentTimeMillis()
)
