package com.winxclub.youkube.common

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.winxclub.youkube.google.oauth.AbstractOAuthServer
import com.winxclub.youkube.google.oauth.events.CodeEvent
import com.winxclub.youkube.google.oauth.events.ErrorEvent
import com.winxclub.youkube.google.oauth.TokenData

class KtorOAuthServer(state: String?) : AbstractOAuthServer(state) {
    val server = embeddedServer(Netty, port, "0.0.0.0") {
        install(ContentNegotiation) {
            json()
        }
        routing {
            get(codeUri) {
                val receivedState = call.parameters["state"]
                if (state != receivedState) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }
                call.parameters["error"]
                    ?.let { events.emit(ErrorEvent(it)) }
                call.parameters["code"]
                    ?.let { events.emit(CodeEvent(receivedState, it)) }
                call.respond(HttpStatusCode.OK)
            }
        }
    }.start(wait = false)

    override fun close() {
        server.stop(1000, 1000)
    }
}