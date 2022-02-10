package io.gianluigip.spectacle.report.api

import io.gianluigip.spectacle.di
import io.gianluigip.spectacle.report.ReportGenerator
import io.gianluigip.spectacle.specification.model.toComponent
import io.gianluigip.spectacle.specification.model.toFeature
import io.gianluigip.spectacle.specification.model.toSource
import io.gianluigip.spectacle.specification.model.toSpecStatus
import io.gianluigip.spectacle.specification.model.toTag
import io.gianluigip.spectacle.specification.model.toTeam
import io.ktor.http.Parameters
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.kodein.di.instance

fun Route.specReportRoutes() {
    val reportGenerator by di.instance<ReportGenerator>()

    route("/report") {
        get {
            val parameters: Parameters = call.request.queryParameters
            val report = reportGenerator.generateReport(
                feature = parameters["feature"]?.toFeature(),
                source = parameters["source"]?.toSource(),
                component = parameters["component"]?.toComponent(),
                tag = parameters["tag"]?.toTag(),
                team = parameters["team"]?.toTeam(),
                status = parameters["status"]?.toSpecStatus(),
            ).toResponse()
            call.respond(report)
        }
    }
}