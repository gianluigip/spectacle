package io.gianluigip.spectacle.report.api

import io.gianluigip.spectacle.di
import io.gianluigip.spectacle.report.InteractionsReportGenerator
import io.gianluigip.spectacle.specification.model.toComponent
import io.gianluigip.spectacle.specification.model.toFeature
import io.gianluigip.spectacle.specification.model.toSource
import io.gianluigip.spectacle.specification.model.toTag
import io.gianluigip.spectacle.specification.model.toTeam
import io.ktor.http.Parameters
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.kodein.di.instance

fun Route.interactionReportRoutes() {
    val reportGenerator by di.instance<InteractionsReportGenerator>()

    route("/report/interactions") {
        get {
            val parameters: Parameters = call.request.queryParameters
            val report = reportGenerator.generateReport(
                features = parameters["features"].splitAndMap { it.toFeature() },
                sources = parameters["sources"].splitAndMap { it.toSource() },
                components = parameters["components"].splitAndMap { it.toComponent() },
                tags = parameters["tags"].splitAndMap { it.toTag() },
                teams = parameters["teams"].splitAndMap { it.toTeam() },
            ).toResponse()
            call.respond(report)
        }
    }
}