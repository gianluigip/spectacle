package io.gianluigip.spectacle.component.components

import csstype.Color
import csstype.pct
import csstype.px
import csstype.vw
import io.gianluigip.spectacle.api.components.ComponentApiReport
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.components.TabList
import io.gianluigip.spectacle.common.components.md
import io.gianluigip.spectacle.common.components.xl
import io.gianluigip.spectacle.common.components.xs
import io.gianluigip.spectacle.common.utils.infoMain
import io.gianluigip.spectacle.common.utils.toDisplay
import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.component.api.Component
import io.gianluigip.spectacle.diagram.components.SystemDiagram
import io.gianluigip.spectacle.events.components.EventAccordion
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.home.Themes
import io.gianluigip.spectacle.report.api.model.ApiReportResponse
import io.gianluigip.spectacle.report.api.model.EventsReportResponse
import io.gianluigip.spectacle.report.api.model.InteractionsReportResponse
import io.gianluigip.spectacle.report.api.model.SpecsReportResponse
import io.gianluigip.spectacle.specification.components.FeaturesReport
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.components.WikiListBrowser
import kotlinx.js.jso
import mui.lab.TabContext
import mui.lab.TabPanel
import mui.material.Box
import mui.material.Grid
import mui.material.GridDirection
import mui.material.Stack
import mui.material.Tab
import mui.material.Typography
import mui.material.styles.Theme
import mui.material.styles.TypographyVariant
import mui.system.responsive
import react.FC
import react.Props
import react.ReactNode
import react.create
import react.dom.onChange
import react.useContext
import react.useState

external interface ComponentViewerProps : Props {
    var component: Component
    var interactions: InteractionsReportResponse
    var events: EventsReportResponse
    var api: ApiReportResponse
    var features: SpecsReportResponse
    var wikiPages: List<WikiPageMetadataResponse>
}

val ComponentViewer = FC<ComponentViewerProps> { props ->
    var tabSelected by useState("Features")
    val theme by useContext(ThemeContext)
    val component = props.component
    val interactions = props.interactions
    val events = props.events
    val api = props.api
    val features = props.features
    val wikiPages = props.wikiPages

    Typography { sx = jso { color = theme.infoMain }; variant = TypographyVariant.h5; +component.name }
    Spacer { height = 10.px }

    Grid {
        container = true
        direction = responsive(GridDirection.row)
        columnSpacing = responsive(Themes.SPACE_PADDING)

        Grid {
            item = true
            xs = 4; md = 3; xl = 2
            +generateMetadataSection(features, theme)
        }

        Grid {
            item = true
            xs = 8; md = 9; xl = 10
            onChange

            Box {
                sx = jso { height = 100.pct; width = 100.pct }

                TabContext {
                    value = tabSelected

                    Box {
                        sx = jso { width = 100.pct; borderBottom = 1.px; borderColor = Color("divider") }

                        TabList {
                            onChange = { _, selected -> tabSelected = selected }
                            Tab { label = "Features".toNode(); value = "Features" }
                            Tab { label = "Diagram".toNode(); value = "Diagram" }
                            Tab { label = "API".toNode(); value = "API" }
                            Tab { label = "Events".toNode(); value = "Events" }
                            Tab { label = "Wiki".toNode(); value = "Wiki" }
                        }
                    }
                    Spacer { height = 10.px }

                    TabPanel {
                        sx = jso { padding = 0.px; width = 100.pct; height = 100.pct }
                        value = "Features"
                        FeaturesReport {
                            this.features = features.features
                            expanded = true
                        }
                    }
                    TabPanel {
                        sx = jso { padding = 0.px; width = 100.pct; height = 100.pct }
                        value = "Diagram"
                        SystemDiagram {
                            this.interactions = interactions.interactions
                            this.components = interactions.filters.components
                        }
                    }
                    TabPanel {
                        sx = jso { padding = 0.px; width = 100.pct; height = 100.pct; maxWidth = 95.vw }
                        value = "API"
                        if (api.components.isEmpty()) {
                            Typography { +"No endpoints found." }
                        } else {
                            ComponentApiReport { this.component = api.components.first() }
                        }
                    }
                    TabPanel {
                        sx = jso { padding = 0.px; width = 100.pct; height = 100.pct }
                        value = "Events"
                        if (events.events.isEmpty()) {
                            Typography { +"No events found." }
                        } else {
                            events.events.forEach { eventResponse ->
                                EventAccordion { event = eventResponse }
                            }
                        }
                    }
                    TabPanel {
                        sx = jso { padding = 0.px; width = 100.pct; height = 100.pct }
                        value = "Wiki"
                        WikiListBrowser { this.wikiPages = wikiPages }
                    }
                }
            }
        }
    }
}

private fun generateMetadataSection(specs: SpecsReportResponse, theme: Theme): ReactNode = Stack.create {
    spacing = responsive(1)

    +generateMetadataBox(
        title = "Tags", content = specs.filters.tags.sorted(), theme
    )
    +generateMetadataBox(
        title = "Teams", content = specs.filters.teams.sorted(), theme
    )
    +generateMetadataBox(
        title = "Updated", content = listOf(specs.maxUpdatedTime().toDisplay()), theme
    )
    +generateMetadataBox(
        title = "Sources", content = specs.filters.sources.sorted(), theme
    )
}

private fun generateMetadataBox(title: String, content: List<String>, theme: Theme): ReactNode =
    Stack.create {
        spacing = responsive(1)
        Typography { sx = jso { color = theme.infoMain }; variant = TypographyVariant.h6; +title }
        content.forEach {
            Typography { +it }
        }
    }

private fun SpecsReportResponse.maxUpdatedTime() = features.flatMap { it.specs }.minOf { it.updateTime }
