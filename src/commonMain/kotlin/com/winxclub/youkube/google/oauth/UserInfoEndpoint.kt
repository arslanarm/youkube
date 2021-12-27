package com.winxclub.youkube.google.oauth

import com.winxclub.youkube.AccessToken
import com.winxclub.youkube.google.oauth.scopes.UserInfoScope
import com.winxclub.youkube.internal.Endpoint
import com.winxclub.youkube.internal.getCustom
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class UserInfoEndpoint(val accessToken: AccessToken) : Endpoint() {
    init {
        require(UserInfoScope in accessToken.scopes)
    }
    suspend fun get(): UserInfo = httpClient.getCustom("https://www.googleapis.com/oauth2/v1/userinfo?alt=json") {
        authorization(accessToken.tokenData)
    }
}

suspend fun AccessToken.getProfile() = UserInfoEndpoint(this).get()

@Serializable
data class UserInfo(
    val id: String,
    val email: String? = null,
    @SerialName("verified_email")
    val verifiedEmail: Boolean? = null,
    val name: String,
    @SerialName("given_name")
    val givenName: String,
    @SerialName("family_name")
    val familyName: String? = null,
    val picture: String,
    val gender: String? = null,
    val locale: String
)