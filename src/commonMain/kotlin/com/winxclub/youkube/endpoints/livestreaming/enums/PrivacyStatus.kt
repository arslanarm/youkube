package com.winxclub.youkube.endpoints.livestreaming.enums

import kotlinx.serialization.Serializable
import com.winxclub.youkube.endpoints.commonSerialization.LowercasedSerializer

object PrivacyStatusSerializer: LowercasedSerializer<PrivacyStatus>("PrivacyStatus", PrivacyStatus.values())

@Serializable(PrivacyStatusSerializer::class)
enum class PrivacyStatus {
    Private,
    Public,
    Unlisted
}