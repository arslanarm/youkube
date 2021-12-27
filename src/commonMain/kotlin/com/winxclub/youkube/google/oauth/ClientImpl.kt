package com.winxclub.youkube.google.oauth

import com.winxclub.youkube.AccessToken
import com.winxclub.youkube.Client
import com.winxclub.youkube.google.oauth.scopes.Scope
import io.ktor.client.features.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
open class ClientImpl(
    override val clientId: String,
    override val clientSecret: String,
    override var tokenData: TokenData,
    override val scopes: Set<Scope>,
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : Client, CoroutineScope by coroutineScope {
    init {
        launch {
            while (true) {
                delay(Duration.seconds(tokenData.expiresIn.toLong()))
                tokenData.refreshToken ?: break
                refreshToken()
            }
        }
    }

    override suspend fun refreshToken() {
        val refreshedToken = try {
            RefreshTokenEndpoint(this@ClientImpl).post()
        } catch (e: ClientRequestException) {
            println("Cannot refresh a token")
            throw e
        }
        val new = tokenData.copy(
            accessToken = refreshedToken.accessToken,
            tokenType = refreshedToken.tokenType,
            expiresIn = refreshedToken.expiresIn
        )
        refreshedToken(tokenData, new)
        tokenData = new
    }

    override fun refreshedToken(old: TokenData?, new: TokenData) {}
}

