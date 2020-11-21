package com.ataei.abbas.karam.utils

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.ataei.abbas.karam.utils.Constants.Companion.PREFERENCE_NAME
import com.ataei.abbas.karam.utils.Constants.Companion.RANSOM_KEY
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferenceRepository @Inject constructor(@ActivityContext context: Context) {
    private val dataStore: DataStore<Preferences> = context.createDataStore(name = PREFERENCE_NAME)

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