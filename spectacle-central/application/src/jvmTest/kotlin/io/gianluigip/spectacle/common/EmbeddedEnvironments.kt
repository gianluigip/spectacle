package io.gianluigip.spectacle.common

import io.gianluigip.spectacle.module
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine

object EmbeddedEnvironments {

    const val PORT = 9090
    private var isStarted = false
    private val SERVER: NettyApplicationEngine = embeddedServer(Netty, port = PORT) { module() }

    @Synchronized
    fun start(init: () -> Unit) {
        if (!isStarted) {
            init.invoke()
            SERVER.start()
            Runtime.getRuntime().addShutdownHook(Thread { SERVER.stop(1000, 2000) })
            isStarted = true
        }
    }

}
