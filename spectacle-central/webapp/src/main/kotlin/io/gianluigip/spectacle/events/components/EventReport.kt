package io.gianluigip.spectacle.events.components

import csstype.px
import io.gianluigip.spectacle.api.components.RequestBody
import io.gianluigip.spectacle.common.components.MetaDataChip
import io.gianluigip.spectacle.common.components.SectionTitle
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.report.api.model.EventReportResponse
import io.gianluigip.spectacle.specification.model.EventFormat
import io.gianluigip.spectacle.wiki.components.MarkdownViewer
import kotlinx.js.jso
import mui.icons.material.ExpandMore
import mui.material.Accordion
import mui.material.AccordionDetails
import mui.material.AccordionSummary
import mui.material.Grid
import mui.material.GridDirection.row
import mui.material.Stack
import mui.material.StackDirection
import mui.material.Typography
import mui.material.styles.TypographyVariant.subtitle1
import mui.system.responsive
import react.FC
import react.Props
import react.create
import react.useContext

external interface EventReportProps : Props {
    var event: EventReportResponse
}

val EventAccordion = FC<EventReportProps> { props ->
    val theme by useContext(ThemeContext)
    val event = props.event

    Accordion {
        AccordionSummary {
            sx = jso {
                color = theme.palette.info.contrastText
                backgroundColor = theme.palette.info.main
            }
            expandIcon = ExpandMore.create()
            Typography { variant = subtitle1; +event.name }
        }

        AccordionDetails {
            Stack {
                spacing = responsive(5.px)

                Grid {
                    container = true
                    spacing = responsive(1)
                    direction = responsive(row)

                    event.format?.let { Grid { item = true; MetaDataChip { label = "Format: $it" } } }
                    if (event.tags.isNotEmpty()) {
                        Grid { item = true; MetaDataChip { label = "Tags: ${event.tags.sorted().joinToString(", ")}" } }
                    }
                    Grid { item = true; MetaDataChip { label = "Features: ${event.features.sorted().joinToString(", ")}" } }
                    Grid { item = true; MetaDataChip { label = "Features: ${event.features.sorted().joinToString(", ")}" } }
                    Grid { item = true; MetaDataChip { label = "Teams: ${event.teams.sorted().joinToString(", ")}" } }
                    Grid { item = true; MetaDataChip { label = "Components: ${event.sources.sorted().joinToString(", ")}" } }
                    Grid { item = true; MetaDataChip { label = "Source: ${event.components.sorted().joinToString(", ")}" } }
                }

                Stack {
                    direction = responsive(StackDirection.row)

                    if (event.producedBy.isNotEmpty()) {
                        Stack {
                            SectionTitle { text = "Produced by:" }
                            MarkdownViewer {
                                content = event.producedBy.joinToString("\n") { "* `${it}`" }
                            }
                        }
                    }

                    if (event.consumedBy.isNotEmpty()) {
                        Stack {
                            SectionTitle { text = "Consumed by:" }
                            MarkdownViewer {
                                content = event.consumedBy.joinToString("\n") { "* `${it}`" }
                            }
                        }
                    }
                }

                val eventType = bodyType(event.format)
                RequestBody { label = "Schema"; body = event.schema; bodyType = eventType }

                if (event.dependencies.isNotEmpty()) {
                    SectionTitle { text = "Dependencies:" }
                    event.dependencies.forEach { RequestBody { body = it; bodyType = eventType } }
                }
            }
        }
    }
}

private fun bodyType(format: EventFormat?) = when (format) {
    EventFormat.PROTOBUF -> "protobuf"
    EventFormat.JSON -> "json"
    EventFormat.AVRO -> "avro"
    else -> null
}
