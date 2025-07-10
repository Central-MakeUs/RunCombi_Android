package com.combo.runcombi.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : AuthDataSource {
    object PreferencesKey {
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")

    }

    override fun getAccessToken(): Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[PreferencesKey.ACCESS_TOKEN]
        }

    override fun getRefreshToken(): Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[PreferencesKey.REFRESH_TOKEN]
        }

    override fun setAccessToken(accessToken: String): Flow<Unit> = flow {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.ACCESS_TOKEN] = accessToken
        }
        emit(Unit)
    }

    override fun setRefreshToken(refreshToken: String): Flow<Unit> = flow {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.REFRESH_TOKEN] = refreshToken
        }
        emit(Unit)
    }
}