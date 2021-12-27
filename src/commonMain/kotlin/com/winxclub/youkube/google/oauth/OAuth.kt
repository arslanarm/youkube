package com.winxclub.youkube.google.oauth

import com.winxclub.youkube.AccessToken
import com.winxclub.youkube.Client
import kotlinx.coroutines.flow.first
import com.winxclub.youkube.common.defaultOAuth
import com.winxclub.youkube.google.oauth.OAuthUrlFactory.Companion.OAuthUrl
import com.winxclub.youkube.google.oauth.events.CodeEvent
import com.winxclub.youkube.google.oauth.prompts.Prompt
import com.winxclub.youkube.google.oauth.scopes.Scope
import kotlinx.coroutines.flow.filterIsInstance

@Suppress("FunctionName")
suspend fun OAuthOwnServer(
    clientId: String,
    clientSecret: String,
    domain: String,
    scopes: Set<Scope>,
    accessType: AccessType? = AccessType.Offline,
    state: String? = null,
    includeGrantedScopes: Boolean? = null,
    loginHint: String? = null,
    prompts: List<Prompt> = listOf(),
    server: OAuthServer = defaultOAuth(state),
    closeServer: Boolean = true,
    shareUrl: suspend (String) -> Unit
): Client = OAuthServer(
    clientId,
    clientSecret,
    "$domain${server.codeUri}",
    scopes = scopes,
    accessType = accessType,
    state = state,
    includeGrantedScopes = includeGrantedScopes,
    loginHint = loginHint,
    prompts = prompts,
    shareUrl = shareUrl,
    getCode = {  val event = server.filterIsInstance<CodeEvent>().first { it.state == state }; event.code }
).also { if (closeServer) server.close() }

suspend fun OAuthServer(
    clientId: String,
    clientSecret: String,
    domain: String,
    scopes: Set<Scope>,
    accessType: AccessType? = AccessType.Offline,
    state: String? = null,
    includeGrantedScopes: Boolean? = null,
    loginHint: String? = null,
    prompts: List<Prompt> = listOf(),
    shareUrl: suspend (String) -> Unit,
    getCode: suspend () -> String
): Client {
    val url = OAuthUrl(
        clientId,
        domain,
        scopes = scopes,
        accessType = accessType,
        state = state,
        includeGrantedScopes = includeGrantedScopes,
        loginHint = loginHint,
        prompts = prompts
    )
    shareUrl(url)
    val code = getCode()
    val tokenData = token(clientId, clientSecret, code, domain)
    return ClientImpl(clientId, clientSecret, tokenData, scopes)
}

class OAuthException(message: String) : Exception(message)