package com.winxclub.youkube.internal

import com.winxclub.youkube.google.oauth.scopes.Scope
import io.ktor.client.*
import io.ktor.client.features.json.*

abstract class Endpoint {
    companion object {
        val httpClient = HttpClient {
            Json {
                kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                }
            }
        }
    }
}
