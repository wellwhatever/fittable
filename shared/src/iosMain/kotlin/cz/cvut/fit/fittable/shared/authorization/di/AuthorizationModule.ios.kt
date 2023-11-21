package cz.cvut.fit.fittable.shared.authorization.di

import cz.cvut.fit.fittable.shared.authorization.data.dataStore
import cz.cvut.fit.fittable.shared.authorization.data.local.authorizationDataStoreFileName
import cz.cvut.fit.fittable.shared.authorization.data.local.usernameDataStoreFileName
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authorizationModule = module {
    single(named("authorization")) { dataStore(authorizationDataStoreFileName) }
    single(named("username")) { dataStore(usernameDataStoreFileName) }
}
