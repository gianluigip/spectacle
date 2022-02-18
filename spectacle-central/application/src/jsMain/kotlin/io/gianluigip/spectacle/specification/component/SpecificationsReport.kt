package io.gianluigip.spectacle.specification.component

import csstype.FlexGrow
import csstype.px
import io.gianluigip.spectacle.common.component.Spacer
import io.gianluigip.spectacle.common.utils.buildReportUrlWithParameters
import io.gianluigip.spectacle.common.utils.escapeSpaces
import io.gianluigip.spectacle.common.utils.parseParams
import io.gianluigip.spectacle.report.api.model.FeatureReportResponse
import io.gianluigip.spectacle.report.api.model.ReportFiltersResponse
import io.gianluigip.spectacle.specification.api.getSpecReport
import io.gianluigip.spectacle.specification.model.SpecStatus
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
import react.router.useLocation
import react.router.useNavigate
import react.useEffect
import react.useState

const val specificationsReportPath = "/specifications"
val SpecificationsReport = FC<Props> {
    val navigate = useNavigate()

    var featuresResponse by useState<List<FeatureReportResponse>>()
    var filtersResponse by useState<ReportFiltersResponse>()
    var currentFilters by useState<FiltersSelected>()
    val queryParams = useLocation().search.parseParams()
    val queryFilters = FiltersSelected(
        feature = queryParams["feature"],
        source = queryParams["source"],
        component = queryParams["component"],
        tag = queryParams["tag"],
        team = queryParams["team"],
        status = queryParams["status"]?.let { status -> SpecStatus.values().firstOrNull { it.name == status } },
    )

    fun loadSpecReport(filters: FiltersSelected) = MainScope().launch {
        val response = getSpecReport(filters.feature, filters.source, filters.component, filters.tag, filters.team, filters.status)
        featuresResponse = response.features
        filtersResponse = response.filters
    }

    fun refreshSearch(filters: FiltersSelected) = navigate.invoke(buildReportUrlWithParameters(specificationsReportPath, filters))

    useEffect {
        if (currentFilters != queryFilters) {
            currentFilters = queryFilters
            loadSpecReport(queryFilters)
        }
    }

    Grid {
        container = true
        spacing = ResponsiveStyleValue(20.px)
        direction = ResponsiveStyleValue(row)
        Grid {
            item = true;
            xs = 4; md = 3; xl = 2
            Typography { variant = "h5"; +"Filters" }
            Spacer { height = 10.px }
            if (featuresResponse == null) {
                LinearProgress { sx = jso { FlexGrow(1.0) } }
            }
            filtersResponse?.let {
                ReportFilters {
                    filtersSelected = currentFilters ?: FiltersSelected()
                    filters = it
                    onFilterChanged = { filters -> refreshSearch(filters) }
                }
            }
        }
        Grid {
            item = true;
            xs = 8; md = 9; xl = 10
            Typography { variant = "h5"; +"List of Features" }
            Spacer { height = 10.px }
            if (featuresResponse == null) {
                LinearProgress { sx = jso { FlexGrow(1.0) } }
            }
            featuresResponse?.let { FeaturesReport { features = it } }
        }
    }

}
