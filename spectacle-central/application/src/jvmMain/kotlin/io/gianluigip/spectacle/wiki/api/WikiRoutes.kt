package io.gianluigip.spectacle.wiki.api

import io.gianluigip.spectacle.di
import io.gianluigip.spectacle.report.api.splitAndMap
import io.gianluigip.spectacle.specification.model.toComponent
import io.gianluigip.spectacle.specification.model.toFeature
import io.gianluigip.spectacle.specification.model.toSource
import io.gianluigip.spectacle.specification.model.toTag
import io.gianluigip.spectacle.specification.model.toTeam
import io.gianluigip.spectacle.wiki.WikiFinder
import io.gianluigip.spectacle.wiki.WikiProcessor
import io.gianluigip.spectacle.wiki.api.model.WikiPageRequest
import io.gianluigip.spectacle.wiki.model.toWikiId
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.kodein.di.instance
import org.slf4j.LoggerFactory

private val LOG = LoggerFactory.getLogger("WikiRoutes")

fun Route.wikiRoutes() {
    val wikiProcessor by di.instance<WikiProcessor>()
    val wikiFinder by di.instance<WikiFinder>()

    route("/wiki") {

        get {
            val parameters: Parameters = call.request.queryParameters
            val wikiPages = wikiFinder.findBy(
                features = parameters["features"].splitAndMap { it.toFeature() },
                sources = parameters["sources"].splitAndMap { it.toSource() },
                components = parameters["components"].splitAndMap { it.toComponent() },
                tags = parameters["tags"].splitAndMap { it.toTag() },
                teams = parameters["teams"].splitAndMap { it.toTeam() },
            ).map { it.toResponse() }
            call.respond(wikiPages)
        }

        get("/{wikiId}") {
            val wikiId: String? = call.parameters["wikiId"]
            if (wikiId == null || wikiId.isEmpty()) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            val page = wikiFinder.findById(wikiId.toWikiId())?.toResponse()
            if (page == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(page)
            }

        }

        post {
            val request = call.receive<WikiPageRequest>().toModel()
            LOG.info("Saving wiki page ${request.title} with checksum ${request.checksum}")
            val pageMetadata = wikiProcessor.save(request).toResponse()
            call.respond(HttpStatusCode.Created, pageMetadata)
        }

        put("/{wikiId}") {
            val wikiId: String? = call.parameters["wikiId"]
            if (wikiId == null || wikiId.isEmpty()) {
                call.respond(HttpStatusCode.NotFound)
                return@put
            }
            val request = call.receive<WikiPageRequest>().toModel()
            val pageMetadata = wikiProcessor.update(wikiId!!.toWikiId(), request).toResponse()
            call.respond(pageMetadata)
        }

        delete("/{wikiId}") {
            val wikiId: String? = call.parameters["wikiId"]
            if (wikiId == null || wikiId.isEmpty()) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }
            wikiProcessor.delete(wikiId!!.toWikiId())
            call.respond(HttpStatusCode.OK)
        }

    }
}
