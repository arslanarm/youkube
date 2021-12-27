package com.winxclub.youkube.google.oauth

import com.winxclub.youkube.google.oauth.prompts.Prompt
import com.winxclub.youkube.google.oauth.scopes.Scope

class OAuthUrlFactory(
    val clientId: String,
    val redirectUri: String,
    val responseType: String = "code",
    val scopes: Set<Scope>,
    val accessType: AccessType? = null,
    val state: String? = null,
    val includeGrantedScopes: Boolean? = null,
    val loginHint: String? = null,
    val prompts: List<Prompt> = listOf()
) {
    companion object {
        @Suppress("FunctionName")
        fun OAuthUrl(
            clientId: String,
            redirectUri: String,
            responseType: String = "code",
            scopes: Set<Scope>,
            accessType: AccessType? = null,
            state: String? = null,
            includeGrantedScopes: Boolean? = null,
            loginHint: String? = null,
            prompts: List<Prompt> = listOf()
        ) = OAuthUrlFactory(
            clientId,
            redirectUri,
            responseType,
            scopes,
            accessType,
            state,
            includeGrantedScopes,
            loginHint,
            prompts
        ).webUrl()
    }

    fun webUrl() = buildString {
        append("https://accounts.google.com/o/oauth2/v2/auth?")
        append("client_id=$clientId&")
        append("redirect_uri=$redirectUri&")
        append("response_type=$responseType&")
        append("scope=${scopes.joinToString(" ") { it.url }}")
        accessType?.let { append("&access_type=${it.value}") }
        state?.let { append("&state=$it") }
        includeGrantedScopes?.let { append("&include_granted_scope=$it") }
        loginHint?.let { append("&login_hint=$it") }
        if (prompts.isNotEmpty()) {
            append("&prompt=${prompts.joinToString(" ") { it.value }}")
        }
    }
}