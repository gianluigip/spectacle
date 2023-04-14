package io.gianluigip.spectacle.specification.components

import csstype.number
import csstype.px
import io.gianluigip.spectacle.common.components.MetaDataChip
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.toDisplay
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.report.api.model.SpecReportResponse
import io.gianluigip.spectacle.specification.toDisplay
import js.core.jso
import mui.material.*
import mui.material.GridDirection.Companion.row
import mui.system.responsive
import react.FC
import react.Props
import react.useRequiredContext

external interface SpecCardProps : Props {
    var spec: SpecReportResponse
}

val SpecCard = FC<SpecCardProps> {
    val theme by useRequiredContext(ThemeContext)
    val spec = it.spec

    Accordion {
        sx = jso { flexGrow = number(1.0) }
        AccordionSummary {
            sx = jso {
                flexGrow = number(1.0)
                color = theme.palette.info.contrastText
                backgroundColor = theme.palette.info.main
            }
            Typography { +spec.name }
        }
        AccordionDetails {
            Grid {
                container = true
                spacing = responsive(1)
                direction = responsive(row)

                if (spec.tags.isNotEmpty()) {
                    Grid { item = true; MetaDataChip { label = "Tags: ${spec.tags.sorted().joinToString(",")}" } }
                }
                Grid { item = true; MetaDataChip { label = "Status: ${spec.status.display}" } }
                Grid { item = true; MetaDataChip { label = "Team: ${spec.team}" } }
                Grid { item = true; MetaDataChip { label = "Component: ${spec.component}" } }
                Grid { item = true; MetaDataChip { label = "Created: ${spec.creationTime.toDisplay()}" } }
                Grid { item = true; MetaDataChip { label = "Updated: ${spec.updateTime.toDisplay()}" } }
                Grid { item = true; MetaDataChip { label = "Source: ${spec.source}" } }
            }
            Spacer { height = 5.px }
            spec.steps.sortedBy { step -> step.index }.forEach { step ->
                Grid {
                    container = true
                    spacing = responsive(0.5)
                    direction = responsive(row)

                    Grid {
                        item = true
                        Typography {
                            sx = jso { color = theme.palette.info.main }
                            +step.type.toDisplay()
                        }
                    }
                    Grid { item = true; Typography { +step.description } }
                }

            }
        }
    }
}