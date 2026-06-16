package com.coffeebliss.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionManager(private val context: Context) {

    companion object {
        val MEMBER_ID_KEY = intPreferencesKey("member_id")
    }

    val memberId: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[MEMBER_ID_KEY] ?: -1
    }

    suspend fun saveMemberId(id: Int) {
        context.dataStore.edit { prefs ->
            prefs[MEMBER_ID_KEY] = id
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
