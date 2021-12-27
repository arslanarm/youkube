package com.winxclub.youkube.google.oauth

import kotlinx.coroutines.flow.SharedFlow
import com.winxclub.youkube.google.oauth.events.OAuthEvent

interface OAuthServer : SharedFlow<OAuthEvent> {
    val port: Int
    val codeUri: String
    fun close()
}