package com.winxclub.youkube.endpoints.data.serialization

import com.winxclub.youkube.endpoints.commonSerialization.Localization
import com.winxclub.youkube.endpoints.commonSerialization.Thumbnail
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
data class Channel(
    val kind: String,
    val etag: String,
    val id: String,
    val snippet: Snippet? = null,
    val contentDetails: ContentDetails? = null,
    val statistics: Statistics? = null,
    val topicDetails: TopicDetails? = null,
    val status: Status? = null,
    val brandingSettings: BrandingSettings? = null,
    val auditDetails: AuditDetails? = null,
    val contentOwnerDetails: ContentOwnerDetails? = null,
    val localizations: Map<String, Localization>? = null
) {
    @Serializable
    data class Snippet(
        val title: String,
        val description: String,
        val customUrl: String? = null,
        val publishedAt: Instant,
        val thumbnails: Map<String, Thumbnail>,
        val defaultLanguage: String? = null,
        val localized: Localization,
        val country: String? = null
    )

    @Serializable
    data class ContentDetails(
        val relatedPlaylists: RelatedPlaylists
    ) {
        @Serializable
        data class RelatedPlaylists(
            val likes: String,
            // val favorites: String, // deprecated
            val uploads: String
        )
    }
    @Serializable
    data class Statistics(
        val viewCount: ULong,
        // val commentCount: ULong // deprecated
        val subscriberCount: ULong,
        val hiddenSubscriberCount: Boolean,
        val videoCount: ULong
    )

    @Serializable
    data class TopicDetails(
        // val topicIds: List<String>, deprecated
        val topicCategories: List<String>
    )

    @Serializable
    data class Status(
        val privacyStatus: String,
        val isLinked: Boolean,
        val longUploadsStatus: String,
        val madeForKids: Boolean,
        val selfDeclaredMadeForKids: Boolean
    )

    @Serializable
    data class BrandingSettings(
        val channel: Channel,
        val watch: Watch
    ) {
        @Serializable
        data class Channel(
            val title: String,
            val description: String,
            val keywords: String,
            val trackingAnalyticsAccountId: String,
            val moderateComments: Boolean,
            val unsubscribedTrailer: String,
            val defaultLanguage: String,
            val country: String
        )

        @Serializable
        data class Watch(
            val textColor: String,
            val backgroundColor: String,
            val featuredPlaylistId: String
        )
    }

    @Serializable
    data class AuditDetails(
        val overallGoodStanding: Boolean,
        val communityGuidelinesGoodStanding: Boolean,
        val copyrightStrikesGoodStanding: Boolean,
        val contentIdClaimsGoodStanding: Boolean,
    )

    @Serializable
    data class ContentOwnerDetails(
        val contentOwner: String,
        val timeLinked: Instant
    )
}