package com.winxclub.youkube.internal

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

val json = Json {
    ignoreUnknownKeys = true
}

public suspend inline fun <reified T> HttpClient.getCustom(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T = get<HttpResponse> {
    url.takeFrom(urlString)
    block()
}.let { json.decodeFromString(it.readText())
}
public suspend inline fun <reified T> HttpClient.postCustom(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T = post<HttpResponse> {
    url.takeFrom(urlString)
    block()
}.let { json.decodeFromString(it.readText()) }