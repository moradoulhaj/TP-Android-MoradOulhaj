package com.example.tpandroidapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.tpandroidapp.data.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Define the singleton DataStore instance once, outside the class:
val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(private val context: Context) {

    private object Keys {
        val USER_ID = intPreferencesKey("user_id")
        val USER_FULLNAME = stringPreferencesKey("user_fullname")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PHONE = stringPreferencesKey("user_phone")
        val USER_ADMIN = stringPreferencesKey("user_admin")
        val USER_CREATED_AT = stringPreferencesKey("user_created_at")
        val USER_UPDATED_AT = stringPreferencesKey("user_updated_at")
        val TOKEN = stringPreferencesKey("token")
    }

    suspend fun saveUser(user: UserData) {
        context.dataStore.edit { prefs ->
            prefs[Keys.USER_ID] = user.id
            prefs[Keys.USER_FULLNAME] = user.fullname
            prefs[Keys.USER_EMAIL] = user.email
            prefs[Keys.USER_PHONE] = user.phone
            prefs[Keys.USER_ADMIN] = user.admin
            prefs[Keys.USER_CREATED_AT] = user.createdAt
            prefs[Keys.USER_UPDATED_AT] = user.updatedAt
            user.token?.let { prefs[Keys.TOKEN] = it }
        }
    }

    val userFlow: Flow<UserData?> = context.dataStore.data.map { prefs ->
        if (prefs[Keys.USER_ID] == null) null else UserData(
            id = prefs[Keys.USER_ID] ?: 0,
            fullname = prefs[Keys.USER_FULLNAME] ?: "",
            email = prefs[Keys.USER_EMAIL] ?: "",
            phone = prefs[Keys.USER_PHONE] ?: "",
            admin = prefs[Keys.USER_ADMIN] ?: "",
            createdAt = prefs[Keys.USER_CREATED_AT] ?: "",
            updatedAt = prefs[Keys.USER_UPDATED_AT] ?: "",
            token = prefs[Keys.TOKEN]
        )
    }

    suspend fun clearUser() {
        context.dataStore.edit { prefs -> prefs.clear() }
    }
}
