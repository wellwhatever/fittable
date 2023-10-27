package cz.cvut.fit.fittable.shared.core.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder

internal class ApiService(
    private val baseUrl: String,
    httpClient: HttpClient,
) {

    private val httpClientInternal = httpClient.config {
        defaultRequest {
            url(URLBuilder(baseUrl).buildString())
        }
    }

    suspend inline fun <reified T> get(
        path: String,
        requestBuilder: HttpRequestBuilder.() -> Unit = {},
    ): T = httpClientInternal.get(path, requestBuilder).body()

    suspend inline fun request(
        path: String,
        method: HttpMethod,
        requestBuilder: HttpRequestBuilder.() -> Unit = {},
    ): HttpResponse {
        return httpClientInternal.request(path) {
            this.method = method
            requestBuilder()
        }
    }
}
