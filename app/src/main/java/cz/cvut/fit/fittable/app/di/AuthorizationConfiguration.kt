package cz.cvut.fit.fittable.app.di

data class AuthorizationConfiguration(
    val clientId: String,
    val scope: String,
    val redirectUri: String,
)
