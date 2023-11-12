package cz.cvut.fit.fittable.shared.authorization.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cz.cvut.fit.fittable.shared.data.createDataStore

fun dataStore(context: Context): DataStore<Preferences> =
    createDataStore(
        producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath },
    )
