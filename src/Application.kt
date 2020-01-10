package com.genedata

import com.fasterxml.jackson.databind.SerializationFeature
import com.genedata.db.Foo
import com.genedata.session.AUTH_PROVIDER
import com.genedata.session.USER_SESSION
import com.genedata.session.UserSession
import com.genedata.session.validator
import com.genedata.ws.connectionManagerActor
import com.genedata.ws.handleConnection
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.close
import io.ktor.http.content.PartData
import io.ktor.jackson.jackson
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import java.time.Duration
import kotlin.collections.set

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val cm = connectionManagerActor()
    install(Sessions) {
        cookie<UserSession>(USER_SESSION) {
            cookie.extensions["SameSite"] = "strict"
        }
    }

    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    install(Authentication) {
        basic(AUTH_PROVIDER) {
            realm = "Ktor Server"
            validate(validator)
        }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    val foo = Foo()

    routing {
        trace { application.log.trace(it.buildText()) }
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        post("/newGroup") {
            val multipart = call.receiveMultipart()
            var name = "";
            var pwd = "";
            while (true) {
                val part = multipart.readPart() ?: break
                if (part is PartData.FormItem) {
                    if (part.name == "name") {
                        name = part.value
                    } else if (part.name == "password") {
                        pwd = part.value
                    }
                }
            }
            val newGroup = foo.newGroup(name, pwd)
            call.respondText(newGroup.toString())
        }

        webSocket("/ws") {
            val session = call.sessions.get<UserSession>()
            if (session == null) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
            } else {
                handleConnection(session.user, cm)
            }
        }

        authenticate(AUTH_PROVIDER) {
            get("/login") {
                val principal = call.principal<UserIdPrincipal>()
                if (principal != null) {
                    call.sessions.set(USER_SESSION, UserSession(principal.name))
                    call.respond(HttpStatusCode.OK)
                }
            }
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

