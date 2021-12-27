package com.winxclub.youkube.google.oauth.events

data class CodeEvent(val state: String?, val code: String) : OAuthEvent()