package io.gianluigip.spectacle.specification.components

import csstype.pct
import csstype.px
import io.gianluigip.spectacle.common.components.LoadingBar
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.buildUrlWithParameters
import io.gianluigip.spectacle.common.utils.parseParams
import io.gianluigip.spectacle.home.Themes.SPACE_PADDING
import io.gianluigip.spectacle.report.api.model.FeatureReportResponse
import io.gianluigip.spectacle.report.api.model.ReportFiltersResponse
import io.gianluigip.spectacle.specification.api.getSpecReport
import io.gianluigip.spectacle.specification.model.SpecStatus
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.js.jso
import mui.material.Grid
import mui.material.GridDirection.row
import mui.material.Paper
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

    fun loadSpecReport(filters: FiltersSelected) {
        currentFilters = queryFilters
        MainScope().launch {
            val response = getSpecReport(filters.feature, filters.source, filters.component, filters.tag, filters.team, filters.status)
            featuresResponse = response.features
            filtersResponse = response.filters
        }
    }

    fun refreshSearch(filters: FiltersSelected) = navigate.invoke(buildUrlWithParameters(specificationsReportPath, filters))

    useEffect {
        if (currentFilters != queryFilters) loadSpecReport(queryFilters)
    }

    Grid {
        container = true
        direction = ResponsiveStyleValue(row)
        columnSpacing = ResponsiveStyleValue(SPACE_PADDING)
        sx = jso { height = 100.pct }

        Grid {
            item = true
            xs = 4; md = 3; xl = 2

            Paper {
                sx = jso { padding = SPACE_PADDING; height = 100.pct }
                elevation = 2

                Typography { variant = "h5"; +"Filters" }
                Spacer { height = 10.px }
                LoadingBar { isLoading = currentFilters != queryFilters }
                filtersResponse?.let {
                    ReportFilters {
                        filtersSelected = currentFilters ?: FiltersSelected()
                        filters = it
                        onFilterChanged = { filters -> refreshSearch(filters) }
                    }
                }
            }
        }

        Grid {
            item = true
            xs = 8; md = 9; xl = 10

            Paper {
                sx = jso { padding = SPACE_PADDING; height = 100.pct }
                elevation = 2
                Typography { variant = "h5"; +"List of Specs by Feature" }
                Spacer { height = 10.px }
                LoadingBar { isLoading = currentFilters != queryFilters }
                featuresResponse?.let { FeaturesReport { features = it } }
            }
        }
    }

}
