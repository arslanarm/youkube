package com.winxclub.youkube.common

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

actual fun StringBuilder.appendUrlEncoded(any: Any) {
    append(URLEncoder.encode(any.toString(), StandardCharsets.UTF_8.toString()))
}