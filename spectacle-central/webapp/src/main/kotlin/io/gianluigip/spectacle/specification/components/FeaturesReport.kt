package io.gianluigip.spectacle.specification.components

import csstype.px
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.report.api.model.FeatureReportResponse
import kotlinx.js.jso
import mui.icons.material.ExpandMore
import mui.material.Accordion
import mui.material.AccordionDetails
import mui.material.AccordionSummary
import mui.material.Typography
import mui.material.styles.TypographyVariant.subtitle1
import react.FC
import react.Props
import react.create
import react.useContext

external interface FeaturesReportProps : Props {
    var features: List<FeatureReportResponse>
}

val FeaturesReport = FC<FeaturesReportProps> {
    val theme by useContext(ThemeContext)
    val features = it.features.sortedBy { feature -> feature.name }

    features.forEach { feature ->
        Accordion {
            AccordionSummary {
                sx = jso {
                    color = theme.palette.info.contrastText
                    backgroundColor = theme.palette.info.main
                }
                expandIcon = ExpandMore.create()
                Typography { variant = subtitle1; +feature.name }
            }
            AccordionDetails {
                if (feature.description.isNotEmpty()) {
                    Typography { +feature.description }
                    Spacer { height = 5.px }
                }
                feature.specs.sortedBy { spec -> spec.name }.forEach { specData ->
                    SpecCard { spec = specData }
                }
            }
        }
    }

}
