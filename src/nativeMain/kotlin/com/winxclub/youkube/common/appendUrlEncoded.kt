package com.winxclub.youkube.common

actual fun StringBuilder.appendUrlEncoded(any: Any) {
    append(any)
}