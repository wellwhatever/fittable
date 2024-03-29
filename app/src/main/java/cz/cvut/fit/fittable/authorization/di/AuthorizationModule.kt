package cz.cvut.fit.fittable.authorization.di

import android.net.Uri
import cz.cvut.fit.fittable.app.di.AuthorizationConfiguration
import cz.cvut.fit.fittable.authorization.data.AuthorizationRequestComposer
import cz.cvut.fit.fittable.authorization.domain.CreateLoginRequestUseCase
import cz.cvut.fit.fittable.authorization.ui.AuthorizationViewModel
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authorizationModule = module {
    single {
        AuthorizationConfiguration(
            clientId = "69be8dc8-2117-4735-adc0-19102e8ef456",
            scope = "urn:ctu:oauth:sirius:personal:read urn:ctu:oauth:sirius:limited-by-idm:read cvut:sirius:limited-by-idm:read",
            redirectUri = "fit-timetable://oauth/callback",
        )
    }
    single {
        AuthorizationService(androidContext())
    }
    singleOf(::AuthorizationRequestComposer)
    single {
        AuthorizationServiceConfiguration(
            Uri.parse("https://auth.fit.cvut.cz/oauth/oauth/authorize"),
            Uri.parse(""), // Not used in implicit OAuth2.0 flow
        )
    }
    viewModelOf(::AuthorizationViewModel)

    factoryOf(::CreateLoginRequestUseCase)
}
