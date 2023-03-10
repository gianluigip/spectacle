package io.gianluigip.spectacle.common

import io.gianluigip.spectacle.common.beans.testDependencies
import io.gianluigip.spectacle.common.utils.CLOCK
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import java.time.Clock

object EmbeddedEnvironments {

    const val PORT = 9090
    private var isStarted = false
    private lateinit var SERVER: NettyApplicationEngine

    @Synchronized
    fun start(init: () -> Map<String, String>) {
        if (!isStarted) {
            initTestDependencies()
            val serverConfig = init.invoke()
            SERVER = buildServer(serverConfig)
            SERVER.start()
            Runtime.getRuntime().addShutdownHook(Thread { SERVER.stop(1000, 2000) })
            isStarted = true
        }
    }

    private fun buildServer(serverConfig: Map<String, String>): NettyApplicationEngine {
        val env = applicationEngineEnvironment {
            config = MapApplicationConfig().apply {
                put("ktor.deployment.port", PORT.toString())
                serverConfig.forEach { (key, value) -> put(key, value) }
            }.withFallback(ApplicationConfig("application.conf"))
            connector {
                port = PORT
            }
        }
        return embeddedServer(Netty, env)
    }

    private fun initTestDependencies() {
        testDependencies = DI.Module("TestDependencies") {
            bindSingleton<Clock>(overrides = true) { CLOCK }
        }
    }

}
