package cz.cvut.fit.fittable.shared.authorization.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cz.cvut.fit.fittable.shared.data.createDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun dataStore(dataStoreName: String): DataStore<Preferences> = createDataStore(
    producePath = {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null,
        )
        requireNotNull(documentDirectory).path + "/$dataStoreName"
    },
)
