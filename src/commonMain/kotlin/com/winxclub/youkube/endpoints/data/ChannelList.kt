package com.winxclub.youkube.endpoints.data

import com.winxclub.youkube.endpoints.commonSerialization.ListInfo
import com.winxclub.youkube.endpoints.data.serialization.Channel
import com.winxclub.youkube.AccessToken
import com.winxclub.youkube.google.oauth.authorization
import com.winxclub.youkube.internal.Endpoint
import com.winxclub.youkube.internal.getCustom
import io.ktor.client.request.*

class ChannelList(val accessToken: AccessToken): Endpoint() {
    suspend fun list(
        parts: List<ChannelListPart>,
        forUsername: String? = null,
        id: String? = null,
        managedByMe: Boolean? = null,
        mine: Boolean? = null,
        hl: String? = null,
        maxResults: UInt? = null,
        onBehalfOfContentOwner: String? = null,
        pageToken: String? = null
    ): ListInfo<Channel> = httpClient.getCustom("https://www.googleapis.com/youtube/v3/channels") {
        authorization(accessToken.tokenData)
        parts.forEach { part ->
            parameter("part", part.name.replaceFirstChar { it.lowercase() })
        }
        parameter("forUsername", forUsername)
        parameter("id", id)
        parameter("managedByMe", managedByMe)
        parameter("mine", mine)
        parameter("hl", hl)
        parameter("maxResults", maxResults)
        parameter("onBehalfOfContentOwner", onBehalfOfContentOwner)
        parameter("pageToken", pageToken)
    }

    suspend fun listForUsername(
        parts: List<ChannelListPart>,
        forUsername: String,
        hl: String? = null,
        maxResults: UInt? = null,
        onBehalfOfContentOwner: String? = null,
        pageToken: String? = null
    ) = list(
        parts,
        forUsername,
        hl = hl,
        maxResults = maxResults,
        onBehalfOfContentOwner = onBehalfOfContentOwner,
        pageToken = pageToken
    )

    suspend fun listById(
        parts: List<ChannelListPart>,
        id: String,
        hl: String? = null,
        maxResults: UInt? = null,
        onBehalfOfContentOwner: String? = null,
        pageToken: String? = null
    ) = list(
        parts,
        id = id,
        hl = hl,
        maxResults = maxResults,
        onBehalfOfContentOwner = onBehalfOfContentOwner,
        pageToken = pageToken
    )

    suspend fun listManagedByMe(
        parts: List<ChannelListPart>,
        managedByMe: Boolean,
        hl: String? = null,
        maxResults: UInt? = null,
        onBehalfOfContentOwner: String? = null,
        pageToken: String? = null
    ) = list(
        parts,
        managedByMe = managedByMe,
        hl = hl,
        maxResults = maxResults,
        onBehalfOfContentOwner = onBehalfOfContentOwner,
        pageToken = pageToken
    )

    suspend fun listMine(
        parts: List<ChannelListPart>,
        hl: String? = null,
        maxResults: UInt? = null,
        onBehalfOfContentOwner: String? = null,
        pageToken: String? = null
    ) = list(
        parts,
        mine = true,
        hl = hl,
        maxResults = maxResults,
        onBehalfOfContentOwner = onBehalfOfContentOwner,
        pageToken = pageToken
    )
}

suspend fun AccessToken.channelsForUsername(
    forUsername: String,
    parts: List<ChannelListPart> = listOf(ChannelListPart.Snippet),
    hl: String? = null,
    maxResults: UInt? = null,
    onBehalfOfContentOwner: String? = null,
    pageToken: String? = null
) = ChannelList(this).listForUsername(
    parts, forUsername, hl, maxResults, onBehalfOfContentOwner, pageToken
)

suspend fun AccessToken.channelsById(
    id: String,
    parts: List<ChannelListPart> = listOf(ChannelListPart.Snippet),
    hl: String? = null,
    maxResults: UInt? = null,
    onBehalfOfContentOwner: String? = null,
    pageToken: String? = null
) = ChannelList(this).listById(
    parts, id, hl, maxResults, onBehalfOfContentOwner, pageToken
)

suspend fun AccessToken.channelsManagedByMe(
    managedByMe: Boolean,
    parts: List<ChannelListPart> = listOf(ChannelListPart.Snippet),
    hl: String? = null,
    maxResults: UInt? = null,
    onBehalfOfContentOwner: String? = null,
    pageToken: String? = null
) = ChannelList(this).listManagedByMe(
    parts, managedByMe, hl, maxResults, onBehalfOfContentOwner, pageToken
)

suspend fun AccessToken.channelsMine(
    parts: List<ChannelListPart> = listOf(ChannelListPart.Snippet),
    hl: String? = null,
    maxResults: UInt? = null,
    onBehalfOfContentOwner: String? = null,
    pageToken: String? = null
) = ChannelList(this).listMine(
    parts, hl, maxResults, onBehalfOfContentOwner, pageToken
)

enum class ChannelListPart {
    AuditDetails,
    BrandingSettings,
    ContentDetails,
    ContentOwnerDetails,
    Id,
    Localizations,
    Snippet,
    Statistics,
    Status,
    TopicDetails
}
