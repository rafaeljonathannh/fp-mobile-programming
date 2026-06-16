package com.coffeebliss.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.coffeebliss.data.local.dao.MemberDao
import com.coffeebliss.data.local.dao.RedeemDao
import com.coffeebliss.data.local.dao.TransactionDao
import com.coffeebliss.data.local.entity.Member
import com.coffeebliss.data.local.entity.Redeem
import com.coffeebliss.data.local.entity.Transaction

@Database(
    entities = [Member::class, Transaction::class, Redeem::class],
    version = 1,
    exportSchema = false
)
abstract class CoffeeBlissDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao
    abstract fun transactionDao(): TransactionDao
    abstract fun redeemDao(): RedeemDao

    companion object {
        @Volatile
        private var INSTANCE: CoffeeBlissDatabase? = null

        fun getDatabase(context: Context): CoffeeBlissDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CoffeeBlissDatabase::class.java,
                    "coffee_bliss_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
