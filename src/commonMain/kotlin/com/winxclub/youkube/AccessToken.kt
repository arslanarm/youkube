package com.winxclub.youkube

import com.winxclub.youkube.google.oauth.TokenData
import com.winxclub.youkube.google.oauth.scopes.Scope


interface Client : AccessToken {
    val clientId: String
    val clientSecret: String
    override var tokenData: TokenData
    override val scopes: Set<Scope>
    suspend fun refreshToken()
    fun refreshedToken(old: TokenData?, new: TokenData)
}
interface AccessToken {
    val scopes: Set<Scope>
    val tokenData: TokenData
}