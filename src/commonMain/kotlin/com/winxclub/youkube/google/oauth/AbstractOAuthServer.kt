package com.winxclub.youkube.google.oauth

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import com.winxclub.youkube.google.oauth.events.OAuthEvent

abstract class AbstractOAuthServer(
    val state: String?,
    protected val events: MutableSharedFlow<OAuthEvent> = MutableSharedFlow()
) : SharedFlow<OAuthEvent> by events, OAuthServer {
    override val port: Int = 1234
    override val codeUri: String = "/code/"
}