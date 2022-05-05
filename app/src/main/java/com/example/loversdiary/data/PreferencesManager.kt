package com.example.loversdiary.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PreferencesManager"

enum class SpinnerChoice { WEEK, MONTH, QUARTER, YEAR}

data class FilterPreferences(val spinnerChoice: SpinnerChoice)

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore("user_preferences")

    val preferencesFlow = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.e(TAG, "Error reading preferences", exception)
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val spinnerChoice = SpinnerChoice.valueOf(
                        preferences[PreferencesKeys.SPINNER_CHOICE] ?: SpinnerChoice.WEEK.name
                )
                FilterPreferences(spinnerChoice)
            }

    suspend fun updateSpinnerChoice(spinnerChoice: SpinnerChoice) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SPINNER_CHOICE] = spinnerChoice.name
        }
    }

    private object PreferencesKeys {
        val SPINNER_CHOICE = preferencesKey<String>("sort_order")
    }
}