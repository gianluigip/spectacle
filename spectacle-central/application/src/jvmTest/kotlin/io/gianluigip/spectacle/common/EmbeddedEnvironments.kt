package io.gianluigip.spectacle.common

import io.gianluigip.spectacle.common.beans.testDependencies
import io.gianluigip.spectacle.common.utils.CLOCK
import io.gianluigip.spectacle.module
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import java.time.Clock

object EmbeddedEnvironments {

    const val PORT = 9090
    private var isStarted = false
    private val SERVER: NettyApplicationEngine = embeddedServer(Netty, port = PORT) { module() }

    @Synchronized
    fun start(init: () -> Unit) {
        if (!isStarted) {
            initTestDependencies()
            init.invoke()
            SERVER.start()
            Runtime.getRuntime().addShutdownHook(Thread { SERVER.stop(1000, 2000) })
            isStarted = true
        }
    }

    private fun initTestDependencies() {
        testDependencies = DI.Module("TestDependencies") {
            bindSingleton<Clock>(overrides = true) { CLOCK }
        }
    }

}
