package com.winxclub.youkube.endpoints.livestreaming.enums

import kotlinx.serialization.Serializable
import com.winxclub.youkube.endpoints.commonSerialization.Serial
import com.winxclub.youkube.endpoints.commonSerialization.SerialSerializer

object ProjectionSerializer : SerialSerializer<Projection>("Projection", Projection.values())

@Serializable(ProjectionSerializer::class)
enum class Projection(override val serial: String) : Serial {
    Rectangular("rectangular"),
    Round("360")
}