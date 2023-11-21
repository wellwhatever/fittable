package cz.cvut.fit.fittable.shared.authorization.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal const val authorizationDataStoreFileName = "authorization.preferences_pb"

class AuthorizationLocalDataSource(
    private val authorizationDataStore: DataStore<Preferences>,
) {
    private val authToken = stringPreferencesKey("auth_token")

    val authorizationTokenFlow: Flow<String> = authorizationDataStore.data.map { preferences ->
        preferences[authToken].orEmpty()
    }

    suspend fun updateAuthorizationToken(value: String) =
        authorizationDataStore.edit { preferences ->
            preferences[authToken] = value
        }
}
