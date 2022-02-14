package io.gianluigip.spectacle.specification.component

import io.gianluigip.spectacle.report.api.model.ReportFiltersResponse
import mui.material.Typography
import react.FC
import react.Props

external interface ReportFilersProps : Props {
    var filters: ReportFiltersResponse
}

val ReportFilers = FC<ReportFilersProps> {
//    Typography { +it.filters.toString() }
}
