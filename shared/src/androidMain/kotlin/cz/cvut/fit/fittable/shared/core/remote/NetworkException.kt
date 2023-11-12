package cz.cvut.fit.fittable.shared.core.remote

import java.net.UnknownHostException

internal actual fun Throwable.isNoInternet(): Boolean = this is UnknownHostException
