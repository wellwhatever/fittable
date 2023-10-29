package cz.cvut.fit.fittable.shared.core.remote

import io.ktor.client.engine.darwin.DarwinHttpRequestException
import platform.Foundation.NSError
import platform.Foundation.NSURLErrorDomain

internal val apiNoInternetException = DarwinHttpRequestException(
    NSError(domain = NSURLErrorDomain, code = -1009L, userInfo = null),
)

internal actual fun Throwable.isNoInternet(): Boolean {
    return this is DarwinHttpRequestException &&
        origin.domain == apiNoInternetException.origin.domain &&
        origin.code == apiNoInternetException.origin.code
}
