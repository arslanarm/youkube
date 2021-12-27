package com.winxclub.youkube.endpoints.livestreaming

import io.ktor.client.request.*
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.winxclub.youkube.endpoints.livestreaming.enums.BanType
import com.winxclub.youkube.endpoints.commonSerialization.Currency
import com.winxclub.youkube.AccessToken
import com.winxclub.youkube.google.oauth.authorization
import com.winxclub.youkube.google.oauth.scopes.YoutubeReadonlyScope
import com.winxclub.youkube.google.oauth.scopes.YoutubeScope
import com.winxclub.youkube.internal.Endpoint
import com.winxclub.youkube.internal.getCustom

class LiveChatMessages(val accessToken: AccessToken) : Endpoint() {
    init {
        require(YoutubeReadonlyScope in accessToken.scopes || YoutubeScope in accessToken.scopes)
    }

    suspend fun list(
        liveChatId: String,
        parts: List<LiveChatPart>,
        currencyLocalization: String? = null,
        maxResults: UInt? = null, // default 500
        pageToken: String? = null,
        profileImageSize: UInt? = null
    ): LiveChatMessageInfo {
        if (maxResults != null) {
            require(maxResults in 200u..2000u)
        }
        if (profileImageSize != null) {
            require(profileImageSize in 16u..720u)
        }
        return httpClient.getCustom("https://www.googleapis.com/youtube/v3/liveChat/messages") {
            authorization(accessToken.tokenData)
            parameter("liveChatId", liveChatId)
            parts.forEach { part ->
                parameter("part", part.name.replaceFirstChar { it.lowercase() })
            }
            parameter("currencyLocalization", currencyLocalization)
            parameter("maxResults", maxResults)
            parameter("pageToken", pageToken)
            parameter("profileImageSize", profileImageSize)
        }
    }
}

suspend fun AccessToken.liveChatMessages(
    liveChatId: String,
    parts: List<LiveChatPart> = listOf(LiveChatPart.Snippet),
    currencyLocalization: String? = null,
    maxResults: UInt? = null, // default 500
    pageToken: String? = null,
    profileImageSize: UInt? = null
) = LiveChatMessages(this).list(
    liveChatId,
    parts,
    currencyLocalization,
    maxResults,
    pageToken,
    profileImageSize
)

enum class LiveChatPart {
    Id, Snippet, AuthorDetails;
}



@Serializable
data class LiveChatMessageInfo(
    val kind: String,
    val etag: String,
    val nextPageToken: String,
    val pollingIntervalMillis: ULong,
    val offlineAt: Instant? = null,
    val pageInfo: Info,
    val items: List<LiveChatMessage>
) {
    @Serializable
    data class Info(
        val totalResults: Int,
        val resultsPerPage: Int
    )
}

@Serializable
data class LiveChatMessage(
    val kind: String,
    val etag: String,
    val id: String,
    val snippet: LiveChatEvent? = null,
    val authorDetails: AuthorDetails? = null
)

@Serializable
data class AuthorDetails(
    val channelId: String,
    val channelUrl: String,
    val displayName: String,
    val profileImageUrl: String,
    val isVerified: Boolean,
    val isChatOwner: Boolean,
    val isChatSponsor: Boolean,
    val isChatModerator: Boolean
)

@Serializable
sealed class LiveChatEvent {
    abstract val liveChatId: String
    abstract val publishedAt: Instant
    abstract val hasDisplayContent: Boolean

}

@Serializable
@SerialName("chatEndedEvent")
data class ChatEndedEvent(
    override val liveChatId: String,
    override val publishedAt: Instant,
    override val hasDisplayContent: Boolean,
) : LiveChatEvent()

@Serializable
@SerialName("messageDeletedEvent")
data class MessageDeletedEvent(
    override val liveChatId: String,
    val authorChannelId: String,
    override val publishedAt: Instant,
    override val hasDisplayContent: Boolean,
    val displayMessage: String,
    val messageDeletedDetails: Details
) : LiveChatEvent() {
    @Serializable
    data class Details(
        val deletedMessageId: String
    )
}

@Serializable
@SerialName("newSponsorEvent")
data class NewSponsorEvent(
    override val liveChatId: String,
    val authorChannelId: String,
    override val publishedAt: Instant,
    override val hasDisplayContent: Boolean,
    val displayMessage: String,
    val newSponsorDetails: Details
) : LiveChatEvent() {
    @Serializable
    data class Details(
        val memberLevelName: String,
        val isUpgrade: Boolean? = null
    )
}

@Serializable
@SerialName("sponsorOnlyModeEndedEvent")
data class SponsorOnlyModeEndedEvent(
    override val liveChatId: String,
    override val publishedAt: Instant,
    override val hasDisplayContent: Boolean,
    val displayMessage: String,
) : LiveChatEvent()

@Serializable
@SerialName("sponsorOnlyModeStartedEvent")
data class SponsorOnlyModeStartedEvent(
    override val liveChatId: String,
    override val publishedAt: Instant,
    override val hasDisplayContent: Boolean,
    val displayMessage: String,
) : LiveChatEvent()

@Serializable
@SerialName("memberMilestoneChatEvent")
data class MemberMilestoneChatEvent(
    override val liveChatId: String,
    override val publishedAt: Instant,
    override val hasDisplayContent: Boolean,
    val displayMessage: String,
    val memberMilestoneChatDetails: Details
) : LiveChatEvent() {
    @Serializable
    data class Details(
        val userComment: String,
        val memberMonth: UInt,
        val memberLevelName: String
    )
}

@Serializable
@SerialName("superChatEvent")
data class SuperChatEvent(
    override val liveChatId: String,
    override val publishedAt: Instant,
    override val hasDisplayContent: Boolean,
    val displayMessage: String,
    val superChatDetails: Details
) : LiveChatEvent() {
    @Serializable
    data class Details(
        val amountMicros: ULong,
        val currency: Currency,
        @SerialName("amountDisplayString")
        val amountDisplayed: String,
        val userComment: String,
        val tier: UInt
    )
}

@Serializable
@SerialName("superStickerEvent")
data class SuperStickerEvent(
    override val liveChatId: String,
    override val publishedAt: Instant,
    override val hasDisplayContent: Boolean,
    val displayMessage: String,
    val superStickerDetails: Details
) : LiveChatEvent() {
    @Serializable
    data class Details(
        val superStickerMetadata: Metadata,
        val amountMicros: ULong,
        val currency: Currency,
        @SerialName("amountDisplayString")
        val amountDisplayed: String,
        val tier: UInt
    ) {
        @Serializable
        data class Metadata(
            val stickerId: String,
            val altText: String,
            val language: String? = null
        )
    }
}

@Serializable
@SerialName("textMessageEvent")
data class TextMessageEvent(
    override val liveChatId: String,
    val authorChannelId: String,
    override val publishedAt: Instant,
    override val hasDisplayContent: Boolean,
    val displayMessage: String,
    val textMessageDetails: Details
) : LiveChatEvent() {
    @Serializable
    data class Details(
        @SerialName("messageText")
        val text: String
    )
}

@Serializable
@SerialName("userBannedEvent")
data class UserBannedEvent(
    override val liveChatId: String,
    val authorChannelId: String,
    override val publishedAt: Instant,
    override val hasDisplayContent: Boolean,
    val displayMessage: String,
    val userBannedDetails: Details
) : LiveChatEvent() {
    @Serializable
    data class Details(
        val bannedUserDetails: BannedUserDetails,
        val banType: BanType,
        val banDurationSeconds: ULong
    ) {
        @Serializable
        data class BannedUserDetails(
            val channelId: String,
            val channelUrl: String,
            val displayName: String,
            val profileImageUrl: String
        )
    }
}

@Serializable
@SerialName("tombstone")
data class Tombstone(
    override val liveChatId: String,
    override val publishedAt: Instant,
    override val hasDisplayContent: Boolean
) : LiveChatEvent()

@Serializable
@SerialName("fanFundingEvent")
data class FanFundingEvent(
    override val liveChatId: String,
    val authorChannelId: String,
    override val publishedAt: Instant,
    override val hasDisplayContent: Boolean,
    val displayMessage: String,
) : LiveChatEvent()