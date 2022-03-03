package io.gianluigip.spectacle.feature.components

import csstype.px
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.infoMain
import io.gianluigip.spectacle.common.utils.toDisplay
import io.gianluigip.spectacle.feature.api.model.FeatureResponse
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.home.Themes
import io.gianluigip.spectacle.report.api.model.InteractionsReportResponse
import io.gianluigip.spectacle.report.api.model.SpecsReportResponse
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import kotlinx.js.jso
import mui.material.Grid
import mui.material.GridDirection
import mui.material.Stack
import mui.material.Typography
import mui.material.styles.Theme
import mui.system.ResponsiveStyleValue
import react.FC
import react.Props
import react.ReactElement
import react.create
import react.useContext

external interface FeatureViewerProps : Props {
    var feature: FeatureResponse
    var specs: SpecsReportResponse
    var interactions: InteractionsReportResponse
    var wikiPages: List<WikiPageMetadataResponse>
}

val FeatureViewer = FC<FeatureViewerProps> { props ->
    val theme by useContext(ThemeContext)
    val feature = props.feature
    val specs = props.specs

    Typography { sx = jso { color = theme.infoMain }; variant = "h5"; +feature.name }
    Typography { +feature.description }
    Spacer { height = 10.px }

    Grid {
        container = true
        direction = ResponsiveStyleValue(GridDirection.row)
        columnSpacing = ResponsiveStyleValue(Themes.SPACE_PADDING)

        Grid {
            item = true
            xs = 4; md = 3; xl = 2

            Stack {
                spacing = ResponsiveStyleValue(1)

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
        }

        Grid {
            item = true
            xs = 8; md = 9; xl = 10

            Typography { variant = "h5"; +"Tabs" }
        }
    }

}

private fun generateMetadataBox(title: String, content: List<String>, theme: Theme): ReactElement =
    Stack.create {
        spacing = ResponsiveStyleValue(1)
        Typography { sx = jso { color = theme.infoMain }; variant = "h6"; +title }
        content.forEach {
            Typography { +it }
        }
    }

private fun SpecsReportResponse.minCreatedTime() = features.flatMap { it.specs }.minOf { it.creationTime }
private fun SpecsReportResponse.maxUpdatedTime() = features.flatMap { it.specs }.minOf { it.updateTime }
