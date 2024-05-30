package com.kiryuha21.jobsearchplatformclient.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tokens")
        private val tokenKey = stringPreferencesKey("refreshToken")
    }

    suspend fun toggleNotifications(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(CurrentUser.info.username)] = enabled
        }
    }

    fun getNotifications(): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[booleanPreferencesKey(CurrentUser.info.username)] ?: true
        }
    }

    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[tokenKey]
        }
    }

    suspend fun updateRefreshToken(newToken: String) {
        context.dataStore.edit { prefs ->
            prefs[tokenKey] = newToken
        }
    }

    suspend fun deleteRefreshToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(tokenKey)
        }
    }
}
