package io.gianluigip.spectacle.specification.component

import csstype.FlexGrow
import csstype.px
import io.gianluigip.spectacle.common.component.Spacer
import io.gianluigip.spectacle.report.api.model.FeatureReportResponse
import io.gianluigip.spectacle.report.api.model.ReportFiltersResponse
import io.gianluigip.spectacle.specification.api.getSpecReport
import kotlinext.js.jso
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.Grid
import mui.material.GridDirection.row
import mui.material.LinearProgress
import mui.material.Typography
import mui.system.ResponsiveStyleValue
import react.FC
import react.Props
import react.useEffectOnce
import react.useState

const val specificationsReportPath = "specifications"
val SpecificationsReport = FC<Props> {
    var featuresResponse by useState<List<FeatureReportResponse>>()
    var filtersResponse by useState<ReportFiltersResponse>()

    useEffectOnce {
        MainScope().launch {
            val response = getSpecReport()
            featuresResponse = response.features
            filtersResponse = response.filters
        }
    }

    Grid {
        container = true
        spacing = ResponsiveStyleValue(10.px)
        direction = ResponsiveStyleValue(row)
        Grid {
            item = true; xs = 2
            Typography { variant = "h5"; +"Filters" }
            Spacer { height = 10.px }
            if (featuresResponse == null) {
                LinearProgress { sx = jso { FlexGrow(1.0) } }
            }
            filtersResponse?.let { ReportFilers { filters = it } }
        }
        Grid {
            item = true; xs = 10
            Typography { variant = "h5"; +"List of Features" }
            Spacer { height = 10.px }
            if (featuresResponse == null) {
                LinearProgress { sx = jso { FlexGrow(1.0) } }
            }
            featuresResponse?.let { FeaturesReport { features = it } }
        }
    }

}
