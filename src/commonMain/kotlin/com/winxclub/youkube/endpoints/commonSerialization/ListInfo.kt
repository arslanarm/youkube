package com.winxclub.youkube.endpoints.commonSerialization

import kotlinx.serialization.Serializable

@Serializable
data class ListInfo<T>(
    val kind: String,
    val etag: String,
    val nextPageToken: String? = null,
    val prevPageToken: String? = null,
    val pageInfo: PageInfo,
    val items: List<T>? = null
)

@Serializable
data class PageInfo(
    val totalResults: Int,
    val resultsPerPage: Int
)