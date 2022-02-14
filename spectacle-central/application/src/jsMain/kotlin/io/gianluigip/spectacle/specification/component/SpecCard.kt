package io.gianluigip.spectacle.specification.component

import csstype.Color
import csstype.FlexGrow
import csstype.px
import io.gianluigip.spectacle.common.component.Spacer
import io.gianluigip.spectacle.common.utils.lighter
import io.gianluigip.spectacle.common.utils.toDisplay
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.report.api.model.SpecReportResponse
import io.gianluigip.spectacle.specification.toDisplay
import kotlinext.js.jso
import mui.material.Accordion
import mui.material.AccordionDetails
import mui.material.AccordionSummary
import mui.material.Grid
import mui.material.GridDirection.row
import mui.material.Typography
import mui.system.ResponsiveStyleValue
import react.FC
import react.Props
import react.useContext

external interface SpecCardProps : Props {
    var spec: SpecReportResponse
}

val SpecCard = FC<SpecCardProps> {
    val theme by useContext(ThemeContext)
    val spec = it.spec

    Accordion {
        sx = jso { flexGrow = FlexGrow(1.0) }
        AccordionSummary {
            sx = jso {
                sx = jso { flexGrow = FlexGrow(1.0) }
                color = Color(theme.palette.primary.contrastText)
                backgroundColor = Color(theme.palette.primary.main).lighter(65)
            }
            Typography { +spec.name }
        }
        AccordionDetails {
            Grid {
                container = true
                spacing = ResponsiveStyleValue(1)
                direction = ResponsiveStyleValue(row)

                Grid { item = true; SpecMetaDataChip { label = "Status: ${spec.status}" } }


                if (spec.tags.isNotEmpty()) {
                    Grid { item = true; SpecMetaDataChip { label = "Tags: ${spec.tags.joinToString { ", " }}" } }
                }
                Grid { item = true; SpecMetaDataChip { label = "Team: ${spec.team}" } }
                Grid { item = true; SpecMetaDataChip { label = "Component: ${spec.component}" } }
                Grid { item = true; SpecMetaDataChip { label = "Source: ${spec.source}" } }
                Grid { item = true; SpecMetaDataChip { label = "Created: ${spec.creationTime.toDisplay()}" } }
                Grid { item = true; SpecMetaDataChip { label = "Updated: ${spec.updateTime.toDisplay()}" } }
            }
            Spacer { height = 5.px }
            spec.steps.sortedBy { step -> step.index }.forEach { step ->
                Grid {
                    container = true
                    spacing = ResponsiveStyleValue(0.5)
                    direction = ResponsiveStyleValue(row)

                    Grid {
                        item = true
                        Typography {
                            sx = jso { color = Color(theme.palette.info.main) }
                            +step.type.toDisplay()
                        }
                    }
                    Grid { item = true; Typography { +step.description } }
                }

            }
        }
    }
}