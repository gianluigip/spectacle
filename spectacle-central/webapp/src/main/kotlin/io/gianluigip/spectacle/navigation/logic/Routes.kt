package io.gianluigip.spectacle.navigation.logic

import io.gianluigip.spectacle.api.components.ApiReportPage
import io.gianluigip.spectacle.auth.components.LoginPage
import io.gianluigip.spectacle.auth.components.RequireAuth
import io.gianluigip.spectacle.component.components.ComponentsPage
import io.gianluigip.spectacle.diagram.components.SystemDiagramPage
import io.gianluigip.spectacle.events.components.EventsReportPage
import io.gianluigip.spectacle.feature.components.FeaturesPage
import io.gianluigip.spectacle.home.components.ErrorPage
import io.gianluigip.spectacle.home.components.LandingView
import io.gianluigip.spectacle.navigation.logic.Paths.apiPath
import io.gianluigip.spectacle.navigation.logic.Paths.componentsPath
import io.gianluigip.spectacle.navigation.logic.Paths.eventsPath
import io.gianluigip.spectacle.navigation.logic.Paths.featuresPath
import io.gianluigip.spectacle.navigation.logic.Paths.loginPath
import io.gianluigip.spectacle.navigation.logic.Paths.specificationsPath
import io.gianluigip.spectacle.navigation.logic.Paths.systemDiagramPath
import io.gianluigip.spectacle.navigation.logic.Paths.wikiPath
import io.gianluigip.spectacle.specification.components.SpecificationsReport
import io.gianluigip.spectacle.wiki.components.WikiBrowser
import react.ReactNode
import react.create

data class UiRoute(
    val name: String,
    val path: String,
    val root: ReactNode,
)

object Paths {
    const val loginPath = "/login"
    const val componentsPath = "/components"
    const val featuresPath = "/features"
    const val specificationsPath = "/specifications"
    const val apiPath = "/api"
    const val eventsPath = "/events"
    const val systemDiagramPath = "/system_diagram"
    const val wikiPath = "/wiki"
}

object Routes {

    val login = UiRoute("Login", loginPath, LoginPage.create())
    val home = UiRoute("Home", "/", LandingView.create().requiredAuth())
    val components = UiRoute("Components", componentsPath, ComponentsPage.create().requiredAuth())
    val features = UiRoute("Features", featuresPath, FeaturesPage.create().requiredAuth())
    val specifications = UiRoute("Specifications", specificationsPath, SpecificationsReport.create().requiredAuth())
    val api = UiRoute("API", apiPath, ApiReportPage.create().requiredAuth())
    val events = UiRoute("Events", eventsPath, EventsReportPage.create().requiredAuth())
    val systemDiagram = UiRoute("System", systemDiagramPath, SystemDiagramPage.create().requiredAuth())
    val wiki = UiRoute("Wiki", wikiPath, WikiBrowser.create().requiredAuth())
    val notFound = UiRoute("Not Found", "*", ErrorPage.create())

    val ALL = listOf(login, components, features, specifications, api, events, systemDiagram, wiki, notFound)
    val MENU = listOf(components, systemDiagram, api, events, features, specifications, wiki)
}

private fun ReactNode.requiredAuth(): ReactNode =
    RequireAuth.create {
        +this@requiredAuth
    }
