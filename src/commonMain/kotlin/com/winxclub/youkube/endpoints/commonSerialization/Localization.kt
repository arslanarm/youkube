package com.winxclub.youkube.endpoints.commonSerialization

import kotlinx.serialization.Serializable

@Serializable
data class Localization(
    val title: String,
    val description: String
)