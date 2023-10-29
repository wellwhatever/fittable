package cz.cvut.fit.fittable.shared.core.remote

class UnexpectedException : RuntimeException()
class NoInternetException : RuntimeException()

class HttpException(code: Int) : RuntimeException()

internal expect fun Throwable.isNoInternet(): Boolean
