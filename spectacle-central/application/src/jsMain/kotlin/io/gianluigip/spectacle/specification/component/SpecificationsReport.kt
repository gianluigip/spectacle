package io.gianluigip.spectacle.specification.component

import csstype.FlexGrow
import csstype.px
import io.gianluigip.spectacle.common.component.Spacer
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
import react.useEffectOnce
import react.useState

const val specificationsReportPath = "/specifications"
val SpecificationsReport = FC<Props> {
    val navigate = useNavigate()

    var featuresResponse by useState<List<FeatureReportResponse>>()
    var filtersResponse by useState<ReportFiltersResponse>()
    val queryParams = useLocation().search.parseParams()
    val currentFilters = FiltersSelected(
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

    fun refreshSearch(filters: FiltersSelected) {
        navigate.invoke(buildReportWithParameters(filters))
        loadSpecReport(filters)
    }

    useEffectOnce { loadSpecReport(currentFilters) }

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
            filtersResponse?.let {
                ReportFilters {
                    filtersSelected = currentFilters
                    filters = it
                    onFilterChanged = { filters -> refreshSearch(filters) }
                }
            }
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

private fun buildReportWithParameters(filters: FiltersSelected): String {
    val params = mutableListOf<String>()
    if (filters.feature != null) params.add("feature=${filters.feature.escapeSpaces()}")
    if (filters.source != null) params.add("source=${filters.source.escapeSpaces()}")
    if (filters.component != null) params.add("component=${filters.component.escapeSpaces()}")
    if (filters.team != null) params.add("team=${filters.team.escapeSpaces()}")
    if (filters.status != null) params.add("status=${filters.status.name}")

    val searchSegment = if (params.isNotEmpty()) {
        "?${params.joinToString("&")}"
    } else ""
    return "$specificationsReportPath$searchSegment"
}
