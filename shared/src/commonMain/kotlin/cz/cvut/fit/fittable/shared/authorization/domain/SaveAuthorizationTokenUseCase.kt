package cz.cvut.fit.fittable.shared.authorization.domain

import cz.cvut.fit.fittable.shared.core.remote.ApiException
import kotlin.coroutines.cancellation.CancellationException

expect class SaveAuthorizationTokenUseCase {
    @Throws(CancellationException::class, ApiException::class)
    suspend operator fun invoke(rawData: String)
}
