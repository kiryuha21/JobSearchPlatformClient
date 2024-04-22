package com.kiryuha21.jobsearchplatformclient.data.local.datastore

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TokenDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tokens")
        private val tokenKey = stringPreferencesKey("refreshToken")
    }

    suspend fun getRefreshToken(scope: CoroutineScope): StateFlow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[tokenKey]
        }.stateIn(scope)
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
