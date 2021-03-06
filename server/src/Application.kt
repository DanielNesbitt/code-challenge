package com.genedata

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.genedata.messages.Json
import com.genedata.models.DB
import com.genedata.models.getUser
import com.genedata.models.listUsersAndAnswers
import com.genedata.models.newUser
import com.genedata.session.USER_SESSION
import com.genedata.session.UserSession
import com.genedata.session.createValidator
import com.genedata.ws.connect
import com.genedata.ws.connectionManagerActor
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.jackson.jackson
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.sessions.*
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import java.io.File
import java.security.SecureRandom
import java.time.Duration
import kotlin.collections.set

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(@Suppress("UNUSED_PARAMETER") testing: Boolean = false) {
    DB.initialize()

    // init
    newUser("daniel", "daniel456", true)
    newUser("alice", "alice456", true)

    Json.initializeMapper()

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
            validate(createValidator())
        }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(CallLogging)

    routing {
        route("api") {
            post("/newUser") {
                val multipart = call.receiveMultipart()
                var name = ""
                var pwd = ""
                while (true) {
                    val part = multipart.readPart() ?: break
                    if (part is PartData.FormItem) {
                        val value = part.value.trim()
                        when (part.name) {
                            "name" -> name = value
                            "password" -> pwd = value
                        }
                    }
                }
                try {
                    if (name.isEmpty() || pwd.isEmpty()) {
                        throw RuntimeException("Empty user name or password not allowed.")
                    }
                    val newUser = newUser(name, pwd)
                    call.respondText("User $newUser created.")
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
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                }

                get("/superSecretAdminRoute") {
                    val session = call.sessions.get<UserSession>();
                    if (session != null && getUser(session.user)!!.isAdmin) {
                        val mapper = jacksonObjectMapper()
                        val node = mapper.nodeFactory.objectNode()
                            .set("userAnswers", mapper.valueToTree<ObjectNode>(listUsersAndAnswers()))
                        call.respondText(Json.toJsonString(node), ContentType.parse("application/json"))
                    } else {
                        call.respond(HttpStatusCode.NotFound);
                    }
                }

                webSocket("/ws") {
                    val session = call.sessions.get<UserSession>()
                    connect(session!!.user, cm)
                }
            }
        }

        static {
            staticRootFolder = File("client")

            file("favicon.ico")
            file("manifest.json")

            static("static") {
                static("js") {
                    files("static/js")
                }
                static("css") {
                    files("static/css")
                }
            }

            static("/") {
                default("index.html")
            }

            static("/*") {
                default("index.html")
            }
        }

    }
}

