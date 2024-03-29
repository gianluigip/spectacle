package io.gianluigip.spectacle.feature.components

import csstype.Color
import csstype.pct
import csstype.px
import io.gianluigip.spectacle.common.components.*
import io.gianluigip.spectacle.common.utils.infoMain
import io.gianluigip.spectacle.common.utils.toDisplay
import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.diagram.components.SystemDiagram
import io.gianluigip.spectacle.feature.api.model.FeatureResponse
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.home.Themes
import io.gianluigip.spectacle.report.api.model.InteractionsReportResponse
import io.gianluigip.spectacle.report.api.model.SpecsReportResponse
import io.gianluigip.spectacle.specification.components.SpecCard
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.components.WikiListBrowser
import js.core.jso
import mui.lab.TabContext
import mui.lab.TabPanel
import mui.material.*
import mui.material.styles.Theme
import mui.material.styles.TypographyVariant.Companion.h5
import mui.material.styles.TypographyVariant.Companion.h6
import mui.system.responsive
import react.*
import react.dom.onChange

external interface FeatureViewerProps : Props {
    var feature: FeatureResponse
    var specs: SpecsReportResponse
    var interactions: InteractionsReportResponse
    var wikiPages: List<WikiPageMetadataResponse>
}

val FeatureViewer = FC<FeatureViewerProps> { props ->
    var tabSelected by useState<String>("Specifications")
    val theme by useRequiredContext(ThemeContext)
    val feature = props.feature
    val specs = props.specs
    val interactions = props.interactions
    val wikiPages = props.wikiPages

    Typography { sx = jso { color = theme.infoMain }; variant = h5; +feature.name }
    Typography { +feature.description }
    Spacer { height = 10.px }

    Grid {
        container = true
        direction = responsive(GridDirection.row)
        columnSpacing = responsive(Themes.SPACE_PADDING)

        Grid {
            item = true
            xs = 4; md = 3; xl = 2
            +generateMetadataSection(specs, theme)
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
                            Tab { label = "Specifications".toNode(); value = "Specifications" }
                            Tab { label = "Diagram".toNode(); value = "Diagram" }
                            Tab { label = "Wiki".toNode(); value = "Wiki" }
                        }
                    }
                    Spacer { height = 10.px }

                    TabPanel {
                        sx = jso { padding = 0.px; width = 100.pct; height = 100.pct }
                        value = "Specifications"
                        specs.features.first().specs.sortedBy { spec -> spec.name }.forEach { specData ->
                            SpecCard { spec = specData }
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
        title = "Created", content = listOf(specs.minCreatedTime().toDisplay()), theme
    )
    +generateMetadataBox(
        title = "Updated", content = listOf(specs.maxUpdatedTime().toDisplay()), theme
    )
    +generateMetadataBox(
        title = "Components", content = specs.filters.components.sorted(), theme
    )
    +generateMetadataBox(
        title = "Sources", content = specs.filters.sources.sorted(), theme
    )
}


private fun generateMetadataBox(title: String, content: List<String>, theme: Theme): ReactNode =
    Stack.create {
        spacing = responsive(1)
        Typography { sx = jso { color = theme.infoMain }; variant = h6; +title }
        content.forEach {
            Typography { +it }
        }
    }

private fun SpecsReportResponse.minCreatedTime() = features.flatMap { it.specs }.minOf { it.creationTime }
private fun SpecsReportResponse.maxUpdatedTime() = features.flatMap { it.specs }.minOf { it.updateTime }
