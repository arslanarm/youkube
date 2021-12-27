package com.winxclub.youkube.endpoints.livestreaming.enums

import kotlinx.serialization.Serializable
import com.winxclub.youkube.endpoints.commonSerialization.LowercasedSerializer

object ClosedCaptionsTypeSerializer : LowercasedSerializer<ClosedCaptionsType>("ClosedCaptionsType", ClosedCaptionsType.values())

@Serializable(ClosedCaptionsTypeSerializer::class)
enum class ClosedCaptionsType {
    ClosedCaptionsDisabled,
    ClosedCaptionsHttpPost,
    ClosedCaptionsEmbedded
}