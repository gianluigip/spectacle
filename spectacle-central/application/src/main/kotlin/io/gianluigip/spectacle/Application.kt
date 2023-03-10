package io.gianluigip.spectacle

import io.gianluigip.spectacle.auth.api.AuthProvider
import io.gianluigip.spectacle.auth.api.loginRoutes
import io.gianluigip.spectacle.common.beans.productionDependencies
import io.gianluigip.spectacle.common.beans.testDependencies
import io.gianluigip.spectacle.common.repository.initDb
import io.gianluigip.spectacle.feature.api.featuresRoutes
import io.gianluigip.spectacle.report.api.apiReportRoutes
import io.gianluigip.spectacle.report.api.eventsReportRoutes
import io.gianluigip.spectacle.report.api.interactionReportRoutes
import io.gianluigip.spectacle.report.api.specReportRoutes
import io.gianluigip.spectacle.specification.api.specificationsRoutes
import io.gianluigip.spectacle.team.api.teamRoutes
import io.gianluigip.spectacle.wiki.api.wikiRoutes
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private lateinit var _di: DI
val di: DI by lazy { _di }

fun Application.module() {
    val serverConfig = environment.config
    install(ContentNegotiation) { json() }
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        anyHost()
    }
    install(Compression) { gzip() }
    install(CallLogging) { level = Level.INFO }
    initDb(serverConfig)
    _di = DI {
        bindSingleton { serverConfig }
        import(productionDependencies())
        import(testDependencies, allowOverride = true)
    }
    install(Authentication) {
        val authProvider by di.instance<AuthProvider>()
        basic {
            realm = "Access to Spectacle API"
            validate { authProvider.findUserByCredentials(it) }
        }
    }
    routing {
        static("/") {
            resource("/", "index.html")
            resource("/index.css", "index.css")
            resource("/favicon.ico", "favicon.ico")
            resource("/webapp.js", "webapp.js")
            resources("web")
        }
        route("/api") {
            loginRoutes()
            authenticate {
                specificationsRoutes()
                specReportRoutes()
                interactionReportRoutes()
                apiReportRoutes()
                eventsReportRoutes()
                wikiRoutes()
                featuresRoutes()
                teamRoutes()
            }
        }
    }
}
