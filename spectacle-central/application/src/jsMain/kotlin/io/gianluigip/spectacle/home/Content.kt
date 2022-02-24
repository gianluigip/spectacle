package io.gianluigip.spectacle.home

import csstype.GridArea
import csstype.pct
import csstype.px
import io.gianluigip.spectacle.diagram.components.SystemDiagramPage
import io.gianluigip.spectacle.diagram.components.systemDiagramPath
import io.gianluigip.spectacle.specification.component.SpecificationsReport
import io.gianluigip.spectacle.specification.component.specificationsReportPath
import io.gianluigip.spectacle.wiki.component.WikiBrowser
import io.gianluigip.spectacle.wiki.component.wikiPath
import kotlinx.js.jso
import mui.material.Paper
import mui.material.PaperVariant
import mui.material.Typography
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
                    gridArea = GridArea(GridAreas.Content)
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
                index = true
                element = LandingView.create()
            }
            Route {
                path = specificationsReportPath
                element = SpecificationsReport.create()
            }
            Route {
                path = systemDiagramPath
                element = SystemDiagramPage.create()
            }
            Route {
                path = wikiPath
                element = WikiBrowser.create()
            }

            Route {
                path = "*"
                element = Typography.create {
                    variant = "h3"
                    +"404 Page Not Found"
                }
            }
        }
    }
}
