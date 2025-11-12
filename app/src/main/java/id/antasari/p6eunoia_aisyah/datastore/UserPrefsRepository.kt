package id.antasari.p6eunoia_aisyah.datastore // (atau package-mu)

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Pastikan nama file datastore-nya "user_prefs"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPrefsRepository(private val context: Context) {

    // Kunci untuk nama (sudah ada)
    private object Keys {
        val USER_NAME = stringPreferencesKey("user_name")
        // --- TAMBAHKAN KUNCI BARU INI ---
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_completed")
    }

    // Flow untuk nama (sudah ada)
    val userNameFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[Keys.USER_NAME] ?: ""
        }

    // --- TAMBAHKAN FLOW BARU INI ---
    // (Ini yang dipanggil oleh `onboardingDone` di ViewModel-mu)
    val onboardingDoneFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[Keys.ONBOARDING_DONE] ?: false
        }

    // Fungsi simpan nama (sudah ada)
    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.USER_NAME] = name
        }
    }

    // --- TAMBAHKAN FUNGSI BARU INI ---
    // (Ini yang dipanggil oleh `finishOnboarding()` di ViewModel-mu)
    suspend fun setOnboardingDone(isDone: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.ONBOARDING_DONE] = isDone
        }
    }
}