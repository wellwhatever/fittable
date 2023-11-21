package cz.cvut.fit.fittable.shared.authorization.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenInformation(
    @SerialName("aud")
    val aud: List<String>,
    @SerialName("authorities")
    val authorities: List<String>,
    @SerialName("client_id")
    val clientId: String,
    @SerialName("exp")
    val exp: Int,
    @SerialName("scope")
    val scope: List<String>,
    @SerialName("user_name")
    val userName: String
)