package com.winxclub.youkube.endpoints.commonSerialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

open class LowercasedSerializer<T: Enum<T>>(className: String, val values: Array<T>) : KSerializer<T> {
    override fun deserialize(decoder: Decoder): T = values.find { it.name.replaceFirstChar { it.lowercase() } == decoder.decodeString() }!!

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(className, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(value.name.replaceFirstChar { it.lowercase() })
    }

}

interface Serial {
    val serial: String
}

open class SerialSerializer<T: Serial>(className: String, val values: Array<T>): KSerializer<T> {
    override fun deserialize(decoder: Decoder): T = values.find { it.serial == decoder.decodeString() }!!

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(className, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(value.serial)
    }
}