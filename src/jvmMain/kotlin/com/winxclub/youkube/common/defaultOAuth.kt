package com.winxclub.youkube.common

import com.winxclub.youkube.google.oauth.OAuthServer

actual fun defaultOAuth(state: String?): OAuthServer = KtorOAuthServer(state)