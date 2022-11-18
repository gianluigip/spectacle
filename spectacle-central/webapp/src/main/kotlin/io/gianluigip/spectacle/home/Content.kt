package io.gianluigip.spectacle.home

import csstype.pct
import csstype.px
import io.gianluigip.spectacle.api.components.ApiReportPage
import io.gianluigip.spectacle.api.components.apiReportPath
import io.gianluigip.spectacle.auth.components.LoginPage
import io.gianluigip.spectacle.auth.components.RequireAuth
import io.gianluigip.spectacle.auth.components.loginPath
import io.gianluigip.spectacle.diagram.components.SystemDiagramPage
import io.gianluigip.spectacle.diagram.components.systemDiagramPath
import io.gianluigip.spectacle.events.components.EventsReportPage
import io.gianluigip.spectacle.events.components.eventsReportPath
import io.gianluigip.spectacle.feature.components.FeaturesPage
import io.gianluigip.spectacle.feature.components.featuresPath
import io.gianluigip.spectacle.specification.components.SpecificationsReport
import io.gianluigip.spectacle.specification.components.specificationsReportPath
import io.gianluigip.spectacle.wiki.components.WikiBrowser
import io.gianluigip.spectacle.wiki.components.wikiPath
import kotlinx.js.jso
import mui.material.Paper
import mui.material.PaperVariant
import mui.material.Typography
import mui.material.styles.TypographyVariant.h3
import mui.system.Box
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML
import react.router.Outlet
import react.router.Route
import react.router.Routes

private val DEFAULT_PADDING = 10.px

val Content = FC<Props> {

    Routes {
        Route {
            path = "/"
            element = Box.create {
                component = ReactHTML.main
                sx = jso {
                    height = 100.pct
                    gridArea = GridAreas.Content
                    padding = DEFAULT_PADDING
                }

                Paper {
                    sx = jso {
                        padding = DEFAULT_PADDING
                        height = 100.pct
                    }
                    variant = PaperVariant.elevation
                    elevation = 0

                    Outlet()
                }
            }

            Route {
                path = loginPath; element = LoginPage.create()
            }
            Route {
                index = true; element = RequireAuth.create { LandingView { } }
            }
            Route {
                path = featuresPath; element = RequireAuth.create { FeaturesPage { } }
            }
            Route {
                path = specificationsReportPath; element = RequireAuth.create { SpecificationsReport { } }
            }
            Route {
                path = apiReportPath; element = RequireAuth.create { ApiReportPage { } }
            }
            Route {
                path = eventsReportPath; element = RequireAuth.create { EventsReportPage { } }
            }
            Route {
                path = systemDiagramPath; element = RequireAuth.create { SystemDiagramPage { } }
            }
            Route {
                path = wikiPath; element = RequireAuth.create { WikiBrowser { } }
            }

            Route {
                path = "*"
                element = Typography.create {
                    variant = h3
                    +"404 Page Not Found"
                }
            }
        }
    }
}
