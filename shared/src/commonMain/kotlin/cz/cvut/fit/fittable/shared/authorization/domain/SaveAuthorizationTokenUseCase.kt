package cz.cvut.fit.fittable.shared.authorization.domain

expect class SaveAuthorizationTokenUseCase {
    suspend operator fun invoke(rawData: String)
}
