package io.gianluigip.spectacle

import io.gianluigip.spectacle.common.beans.productionDependencies
import io.gianluigip.spectacle.common.beans.testDependencies
import io.gianluigip.spectacle.common.repository.initDb
import io.gianluigip.spectacle.report.api.interactionReportRoutes
import io.gianluigip.spectacle.report.api.specReportRoutes
import io.gianluigip.spectacle.specification.api.specificationsRoutes
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.http.content.resource
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.plugins.CORS
import io.ktor.server.plugins.CallLogging
import io.ktor.server.plugins.Compression
import io.ktor.server.plugins.ContentNegotiation
import io.ktor.server.plugins.gzip
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.kodein.di.DI
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private lateinit var _di: DI
val di: DI by lazy { _di }

fun Application.module() {
    install(ContentNegotiation) { json() }
    install(CORS) {
        method(HttpMethod.Get)
        method(HttpMethod.Put)
        method(HttpMethod.Post)
        method(HttpMethod.Delete)
        header(HttpHeaders.ContentType)
        anyHost()
    }
    install(Compression) { gzip() }
    install(CallLogging) { level = Level.INFO }
    initDb()
    _di = DI {
        import(productionDependencies())
        import(testDependencies, allowOverride = true)
    }
    routing {
        static("/") {
            resource("/", "index.html")
            resource("/index.css", "index.css")
            resource("/favicon.ico", "favicon.ico")
            resource("/application.js", "application.js")
            resources("web")
        }
        route("/api") {
            specificationsRoutes()
            specReportRoutes()
            interactionReportRoutes()
        }
    }
}
