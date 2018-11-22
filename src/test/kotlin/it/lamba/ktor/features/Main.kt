package it.lamba.ktor.features

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.tomcat.Tomcat

fun main(args: Array<String>) {
    embeddedServer(Tomcat, port = 8000, host = "127.0.0.1"){
        install(SinglePageApplication){
            ignoreIfContains = Regex("\\..*$")
        }
        routing {
            route("/ciao") {
                get {
                    call.respondText { "omg it works" }
                }
            }
        }
    }.start(true)
}