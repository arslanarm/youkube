package com.winxclub.youkube.endpoints.livestreaming.enums

import com.winxclub.youkube.endpoints.commonSerialization.LowercasedSerializer
import kotlinx.serialization.Serializable

object BanTypeSerializer : LowercasedSerializer<BanType>("BanType", BanType.values())

@Serializable(BanTypeSerializer::class)
enum class BanType { Permanent, Temporary }