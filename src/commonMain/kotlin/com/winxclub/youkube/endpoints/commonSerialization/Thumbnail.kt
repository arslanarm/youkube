package com.winxclub.youkube.endpoints.commonSerialization

import kotlinx.serialization.Serializable

@Serializable
data class Thumbnail(
    val url: String,
    val width: UInt,
    val height: UInt
)