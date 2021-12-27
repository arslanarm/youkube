package com.winxclub.youkube.endpoints.livestreaming.enums

import kotlinx.serialization.Serializable
import com.winxclub.youkube.endpoints.commonSerialization.LowercasedSerializer

object LiveCycleStatusSerializer : LowercasedSerializer<LiveCycleStatus>("LiveCycleStatus", LiveCycleStatus.values())

@Serializable(LiveCycleStatusSerializer::class)
enum class LiveCycleStatus {
    Complete,
    Created,
    Live,
    LiveStarting,
    Ready,
    Revoked,
    TestStarting,
    Testing
}