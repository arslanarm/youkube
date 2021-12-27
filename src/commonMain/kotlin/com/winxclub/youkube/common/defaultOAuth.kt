package com.winxclub.youkube.common

import com.winxclub.youkube.google.oauth.OAuthServer

expect fun defaultOAuth(state: String?): OAuthServer