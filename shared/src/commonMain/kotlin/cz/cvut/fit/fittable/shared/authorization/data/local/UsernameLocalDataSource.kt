package cz.cvut.fit.fittable.shared.authorization.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal const val usernameDataStoreFileName = "username.preferences_pb"

class UsernameLocalDataSource(
    private val usernameDataStore: DataStore<Preferences>,
) {
    private val username = stringPreferencesKey("username")

    val usernameFlow: Flow<String> = usernameDataStore.data.map { preferences ->
        preferences[username].orEmpty()
    }

    suspend fun updateUsername(value: String) =
        usernameDataStore.edit { preferences ->
            preferences[username] = value
        }
}
