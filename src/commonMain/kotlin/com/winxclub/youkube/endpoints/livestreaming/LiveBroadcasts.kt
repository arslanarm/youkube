package com.winxclub.youkube.endpoints.livestreaming

import com.winxclub.youkube.endpoints.commonSerialization.ListInfo
import com.winxclub.youkube.endpoints.commonSerialization.Thumbnail
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import com.winxclub.youkube.endpoints.livestreaming.enums.*
import com.winxclub.youkube.AccessToken
import com.winxclub.youkube.google.oauth.authorization
import com.winxclub.youkube.google.oauth.scopes.YoutubeReadonlyScope
import com.winxclub.youkube.google.oauth.scopes.YoutubeScope
import com.winxclub.youkube.internal.Endpoint
import com.winxclub.youkube.internal.getCustom
import kotlinx.datetime.Instant

class LiveBroadcasts(val accessToken: AccessToken) : Endpoint() {
    init {
        require(listOf(
            YoutubeScope,
            YoutubeReadonlyScope
        ).any { it in accessToken.scopes})
    }
    enum class BroadcastStatus(val serial: String) {
        Active("active"),
        All("all"),
        Completed("completed"),
        Upcoming("upcoming")
    }

    enum class BroadcastType(val serial: String) {
        All("all"),
        Event("event"),
        Persistent("persistent")
    }

    suspend fun list(
        parts: List<LiveBroadcastPart>,
        broadcastStatus: BroadcastStatus? = null,
        id: String? = null,
        mine: Boolean? = null,
        broadcastType: BroadcastType? = null,
        maxResults: UInt? = null,
        onBehalfOfContentOwner: String? = null,
        onBehalfOfContentOwnerChannel: String? = null,
        pageToken: String? = null
    ): ListInfo<LiveBroadcast> = httpClient.getCustom("https://www.googleapis.com/youtube/v3/liveBroadcasts") {
        authorization(accessToken.tokenData)
        parts.forEach { part ->
            parameter("part", part.name.replaceFirstChar { it.lowercase() })
        }
        parameter("broadcastStatus", broadcastStatus?.serial)
        parameter("id", id)
        parameter("mine", mine)
        parameter("broadcastType", broadcastType?.serial)
        parameter("maxResults", maxResults)
        parameter("onBehalfOfContentOwner", onBehalfOfContentOwner)
        parameter("onBehalfOfContentOwnerChannel", onBehalfOfContentOwnerChannel)
        parameter("pageToken", pageToken)
    }

    suspend fun listById(
        parts: List<LiveBroadcastPart>,
        id: String,
        broadcastType: BroadcastType? = null,
        maxResults: UInt? = null,
        onBehalfOfContentOwner: String? = null,
        onBehalfOfContentOwnerChannel: String? = null,
        pageToken: String? = null
    ) = list(
        parts,
        id = id,
        broadcastType = broadcastType,
        maxResults = maxResults,
        onBehalfOfContentOwner = onBehalfOfContentOwner,
        onBehalfOfContentOwnerChannel = onBehalfOfContentOwnerChannel,
        pageToken = pageToken
    )

    suspend fun listByStatus(
        parts: List<LiveBroadcastPart>,
        broadcastStatus: BroadcastStatus,
        broadcastType: BroadcastType? = null,
        maxResults: UInt? = null,
        onBehalfOfContentOwner: String? = null,
        onBehalfOfContentOwnerChannel: String? = null,
        pageToken: String? = null
    ) = list(
        parts,
        broadcastStatus = broadcastStatus,
        broadcastType = broadcastType,
        maxResults = maxResults,
        onBehalfOfContentOwner = onBehalfOfContentOwner,
        onBehalfOfContentOwnerChannel = onBehalfOfContentOwnerChannel,
        pageToken = pageToken
    )
    suspend fun listMine(
        parts: List<LiveBroadcastPart>,
        broadcastType: BroadcastType? = null,
        maxResults: UInt? = null,
        onBehalfOfContentOwner: String? = null,
        onBehalfOfContentOwnerChannel: String? = null,
        pageToken: String? = null
    ) = list(
        parts,
        mine = true,
        broadcastType = broadcastType,
        maxResults = maxResults,
        onBehalfOfContentOwner = onBehalfOfContentOwner,
        onBehalfOfContentOwnerChannel = onBehalfOfContentOwnerChannel,
        pageToken = pageToken
    )
}

suspend fun AccessToken.liveBroadcastsById(
    id: String,
    parts: List<LiveBroadcastPart> = listOf(LiveBroadcastPart.Snippet),
    broadcastType: LiveBroadcasts.BroadcastType? = null,
    maxResults: UInt? = null,
    onBehalfOfContentOwner: String? = null,
    onBehalfOfContentOwnerChannel: String? = null,
    pageToken: String? = null
) = LiveBroadcasts(this).listById(
    parts,
    id,
    broadcastType,
    maxResults,
    onBehalfOfContentOwner,
    onBehalfOfContentOwnerChannel,
    pageToken
)

suspend fun AccessToken.liveBroadcastsMine(
    parts: List<LiveBroadcastPart> = listOf(LiveBroadcastPart.Snippet),
    broadcastType: LiveBroadcasts.BroadcastType? = null,
    maxResults: UInt? = null,
    onBehalfOfContentOwner: String? = null,
    onBehalfOfContentOwnerChannel: String? = null,
    pageToken: String? = null
) = LiveBroadcasts(this).listMine(
    parts,
    broadcastType,
    maxResults,
    onBehalfOfContentOwner,
    onBehalfOfContentOwnerChannel,
    pageToken
)

suspend fun AccessToken.liveBroadcastsByStatus(
    broadcastStatus: LiveBroadcasts.BroadcastStatus,
    parts: List<LiveBroadcastPart> = listOf(LiveBroadcastPart.Snippet),
    broadcastType: LiveBroadcasts.BroadcastType? = null,
    maxResults: UInt? = null,
    onBehalfOfContentOwner: String? = null,
    onBehalfOfContentOwnerChannel: String? = null,
    pageToken: String? = null
) = LiveBroadcasts(this).listByStatus(
    parts,
    broadcastStatus,
    broadcastType,
    maxResults,
    onBehalfOfContentOwner,
    onBehalfOfContentOwnerChannel,
    pageToken
)

enum class LiveBroadcastPart {
    Id, Snippet, ContentDetails, Status
}

@Serializable
data class LiveBroadcast(
    val kind: String,
    val etag: String,
    val id: String,
    val snippet: Snippet? = null,
    val status: Status? = null,
    val contentDetails: ContentDetails? = null,
    val statistics: Statistics? = null
) {
    @Serializable
    data class Snippet(
        val publishedAt: Instant,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnails: Map<String, Thumbnail>,
        val scheduledStartTime: Instant? = null,
        val scheduledEndTime: Instant? = null,
        val actualStartTime: Instant? = null,
        val actualEndTime: Instant? = null,
        val isDefaultBroadcast: Boolean,
        val liveChatId: String
    )

    @Serializable
    data class Status(
        val lifeCycleStatus: LiveCycleStatus,
        val privacyStatus: PrivacyStatus,
        val recordingStatus: RecordingStatus,
        val madeForKids: Boolean,
        val selfDeclaredMadeForKids: Boolean,
    )

    @Serializable
    data class ContentDetails(
        val boundStreamId: String,
        val boundStreamLastUpdateTimeMs: Instant,
        val monitorStream: MonitorStream,
        val enableEmbed: Boolean,
        val enableDvr: Boolean,
        val enableContentEncryption: Boolean,
        val startWithSlate: Boolean,
        val recordFromStart: Boolean,
        val enableClosedCaptions: Boolean?,
        val closedCaptionsType: ClosedCaptionsType,
        val projection: Projection,
        val enableLowLatency: Boolean,
        val latencyPreference: String,
        val enableAutoStart: Boolean,
        val enableAutoStop: Boolean,
    ) {
        @Serializable
        data class MonitorStream(
            val enableMonitorStream: Boolean,
            val broadcastStreamDelayMs: UInt,
            val embedHtml: String
        )
    }

    @Serializable
    data class Statistics(
        val totalChatCount: ULong
    )
}


