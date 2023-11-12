package cz.cvut.fit.fittable.shared.core.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private val defaultConnectionTimeout = 10.seconds

internal fun provideHttpClient() = HttpClient(OkHttp) {
    engine {
        config {
            retryOnConnectionFailure(true)
            connectTimeout(defaultConnectionTimeout.toJavaDuration())
        }
    }
}
