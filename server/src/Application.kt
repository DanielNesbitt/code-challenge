package com.genedata

import com.fasterxml.jackson.databind.SerializationFeature
import com.genedata.db.DB
import com.genedata.session.USER_SESSION
import com.genedata.session.UserSession
import com.genedata.ws.connectionManagerActor
import com.genedata.ws.handleConnection
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.jackson.jackson
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.sessions.*
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import java.security.SecureRandom
import java.time.Duration
import kotlin.collections.set

val db = DB()

fun main(args: Array<String>) {
    db.newGroup("daniel", "daniel")
    db.newGroup("alice", "alice")
    io.ktor.server.netty.EngineMain.main(args)
}

val validator: suspend ApplicationCall.(UserPasswordCredential) -> Principal? = {
    val group = db.getGroup(it.name)
    if (group != null && group.password == it.password)
        UserIdPrincipal(it.name)
    else null
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(@Suppress("UNUSED_PARAMETER") testing: Boolean = false) {
    val cm = connectionManagerActor()
    install(Sessions) {
        val random = SecureRandom()
        val secretHashKey = ByteArray(20)
        random.nextBytes(secretHashKey)

        cookie<UserSession>(USER_SESSION) {
            cookie.extensions["SameSite"] = "strict"
            transform(SessionTransportTransformerMessageAuthentication(secretHashKey, "HmacSHA256"))
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
        form("formAuth") {
            userParamName = "name"
            passwordParamName = "password"
            skipWhen { it.sessions.get<UserSession>() != null }
            validate(validator)
        }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        post("/newGroup") {
            val multipart = call.receiveMultipart()
            var name = ""
            var pwd = ""
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
            try {
                val newGroup = db.newGroup(name, pwd)
                call.respondText("Group ${newGroup} created.")
            } catch (th: Throwable) {
                call.respond(HttpStatusCode.BadRequest, th.message ?: "")
            }
        }

        get("/user") {
            val session = call.sessions.get<UserSession>()
            call.respond(HttpStatusCode.OK, session?.user.orEmpty())
        }

        authenticate("formAuth") {
            post("/login") {
                val principal = call.principal<UserIdPrincipal>()
                if (principal != null) {
                    call.sessions.set(USER_SESSION, UserSession(principal.name))
                    call.respond(HttpStatusCode.OK, principal.name)
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                }
            }

            webSocket("/ws") {
                val session = call.sessions.get<UserSession>()
                handleConnection(session!!.user, cm)
            }
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

