package com.ataei.abbas.karam.utils

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.ataei.abbas.karam.utils.Constants.Companion.PREFERENCE_NAME
import com.ataei.abbas.karam.utils.Constants.Companion.RANSOM_KEY
import com.ataei.abbas.karam.utils.Constants.Companion.UI_KEY
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferenceRepository @Inject constructor(@ActivityContext context: Context) {
    private val dataStore: DataStore<Preferences> = context.createDataStore(name = PREFERENCE_NAME)

    companion object {
        val IS_DARK_MODE = preferencesKey<Boolean>("dark_mode")
    }

    val uiModeFlow: Flow<UiMode> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference ->
            val isDarkMode = preference[IS_DARK_MODE] ?: false

            when (isDarkMode) {
                true -> UiMode.DARK
                false -> UiMode.LIGHT
            }
        }

    suspend fun setUiMode(uiMode: UiMode) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = when (uiMode) {
                UiMode.LIGHT -> false
                UiMode.DARK -> true
            }
        }
    }

    suspend fun saveUiMode(darkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[preferencesKey<Boolean>(UI_KEY)] = darkMode
        }
    }

    fun getUiMode() : Flow<Boolean> = dataStore.data
        .catch {
            emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[preferencesKey(UI_KEY)] ?: false
        }

    suspend fun saveRansom(ransom: String) {
        dataStore.edit { preferences ->
            preferences[preferencesKey<String>(RANSOM_KEY)] = ransom
        }
    }

    fun getRansom() : Flow<String> = dataStore.data
        .catch {
            emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[preferencesKey(RANSOM_KEY)] ?: "هزار تومان"
        }
}