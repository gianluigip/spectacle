package io.gianluigip.shopping

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

val shoppingList = mutableListOf(
    ShoppingListItem("Cucumbers 🥒", 1),
    ShoppingListItem("Tomatoes 🍅", 2),
    ShoppingListItem("Orange Juice 🍊", 3)
)

fun Application.shoppingTutorialRoutes() {
    routing {
        route("/shopping") {
            get("/hello") {
                call.respondText("Hello, API!")
            }
            route("/shoppingList") {
                get {
                    call.respond(shoppingList)
                }
                post {
                    shoppingList += call.receive<ShoppingListItem>()
                    call.respond(HttpStatusCode.OK)
                }
                delete("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    shoppingList.removeIf { it.id == id }
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}