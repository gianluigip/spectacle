package io.gianluigip.journal

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.html.a
import kotlinx.html.b
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.p

fun Application.journalTutorialRoutes() {
    routing {
        getJournalRoute()
        submitRoute()
    }
}


private fun Routing.getJournalRoute() {
    get("/journal") {
        call.respond(FreeMarkerContent("journal/index.ftl", mapOf("entries" to blogEntries), ""))
    }

}

private fun Routing.submitRoute() {
    post("/journal/submit") {
        val params = call.receiveParameters()
        val headline = params["headline"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val body = params["body"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val newEntry = BlogEntry(headline, body)
        blogEntries.add(0, newEntry)
        call.respondHtml {
            body {
                h1 {
                    +"Thanks for submitting your entry!"
                }
                p {
                    +"We've submitted your new entry titled "
                    b {
                        +newEntry.headline
                    }
                }
                p {
                    +"You have submitted a total of ${blogEntries.count()} articles!"
                }
                a("/journal") {
                    +"Go back"
                }
            }
        }
    }
}