package com.winxclub.youkube.endpoints.livestreaming.enums

import kotlinx.serialization.Serializable
import com.winxclub.youkube.endpoints.commonSerialization.LowercasedSerializer

object RecordingStatusSerializer : LowercasedSerializer<RecordingStatus>("RecordingStatus", RecordingStatus.values())

@Serializable(RecordingStatusSerializer::class)
enum class RecordingStatus {
    NotRecording,
    Recorded,
    Recording
}