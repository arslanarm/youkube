package com.winxclub.youkube.google.oauth

import com.winxclub.youkube.AccessToken
import io.ktor.client.request.*
import com.winxclub.youkube.internal.Endpoint
import com.winxclub.youkube.internal.json
import com.winxclub.youkube.internal.postCustom
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString

object TokenEndpoint : Endpoint() {



    suspend fun post(
        clientId: String,
        clientSecret: String,
        code: String,
        redirectUri: String
    ): TokenData = httpClient.postCustom("https://oauth2.googleapis.com/token") {
        url.parameters.urlEncodingOption = UrlEncodingOption.NO_ENCODING
        parameter("client_id", clientId)
        parameter("client_secret", clientSecret)
        parameter("code", code)
        parameter("redirect_uri", redirectUri)
        parameter("grant_type", "authorization_code")
    }
}

internal suspend fun token(
    clientId: String,
    clientSecret: String,
    code: String,
    redirectUri: String
) = TokenEndpoint.post(clientId, clientSecret, code, redirectUri)

@Serializable
data class TokenData(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: UInt,
    @SerialName("token_type")
    val tokenType: String,
    val scope: String,
    @SerialName("refresh_token")
    val refreshToken: String? = null,
    @SerialName("id_token")
    val idToken: String? = null
)

fun HttpRequestBuilder.authorization(tokenData: TokenData) {
    header("Authorization", "${tokenData.tokenType} ${tokenData.accessToken}")
}