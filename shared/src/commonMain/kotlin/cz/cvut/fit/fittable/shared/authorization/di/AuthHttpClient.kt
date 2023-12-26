package cz.cvut.fit.fittable.shared.authorization.di

import cz.cvut.fit.fittable.shared.authorization.data.local.AuthorizationLocalDataSource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

internal fun provideAuthHttpClient(
    authorizationLocalDataSource: AuthorizationLocalDataSource,
    baseHttpClient: HttpClient,
): HttpClient = baseHttpClient.config {
    install(Logging) {
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            },
        )
    }
    install(Auth) {
        bearer {
            loadTokens {
                val token = authorizationLocalDataSource.authorizationTokenFlow.first()
                BearerTokens(
                    accessToken = token,
                    refreshToken = token,
                )
            }
        }
    }
}
