package io.gianluigip.spectacle.components.specification

import mui.material.Typography
import react.FC
import react.Props

val specificationsReportPath = "specifications"
val SpecificationsReport = FC<Props> {
    Typography {
        variant = "h2"
        +"Specifciations"
    }
}
