package com.winxclub.youkube.google.oauth

import com.winxclub.youkube.Client
import com.winxclub.youkube.internal.Endpoint
import com.winxclub.youkube.internal.postCustom
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class RefreshTokenEndpoint(val client: Client) : Endpoint() {
    suspend fun post(): RefreshedToken = httpClient.postCustom("https://accounts.google.com/o/oauth2/token") {
        parameter("client_id", client.clientId)
        parameter("client_secret", client.clientSecret)
        parameter("refresh_token", client.tokenData.refreshToken)
        parameter("grant_type", "refresh_token")
    }
}

@Serializable
data class RefreshedToken(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: UInt,
    @SerialName("token_type")
    val tokenType: String
)