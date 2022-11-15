package io.gianluigip.spectacle.api.components

import csstype.pct
import csstype.px
import io.gianluigip.spectacle.api.api.getApiReport
import io.gianluigip.spectacle.common.components.LoadingBar
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.components.md
import io.gianluigip.spectacle.common.components.xl
import io.gianluigip.spectacle.common.components.xs
import io.gianluigip.spectacle.common.utils.buildUrlWithParameters
import io.gianluigip.spectacle.common.utils.parseParams
import io.gianluigip.spectacle.home.Themes
import io.gianluigip.spectacle.report.api.model.ComponentApiResponse
import io.gianluigip.spectacle.report.api.model.ReportFiltersResponse
import io.gianluigip.spectacle.specification.components.FiltersSelected
import io.gianluigip.spectacle.specification.components.ReportFilters
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.js.jso
import mui.material.Grid
import mui.material.GridDirection
import mui.material.Paper
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.responsive
import react.FC
import react.Props
import react.router.useLocation
import react.router.useNavigate
import react.useEffect
import react.useState

const val apiReportPath = "/api"
val ApiReportPage = FC<Props> {
    val navigate = useNavigate()

    var componentsApi by useState<List<ComponentApiResponse>>()
    var filtersResponse by useState<ReportFiltersResponse>()
    var currentFilters by useState<FiltersSelected>()
    val queryParams = useLocation().search.parseParams()
    val queryFilters = FiltersSelected(
        searchText = queryParams["searchText"],
        feature = queryParams["feature"],
        source = queryParams["source"],
        component = queryParams["component"],
        tag = queryParams["tag"],
        team = queryParams["team"],
    )

    fun loadApiReport(filters: FiltersSelected) {
        componentsApi = null
        currentFilters = queryFilters
        MainScope().launch {
            val response = getApiReport(
                filters.searchText,
                filters.feature,
                filters.source,
                filters.component,
                filters.tag,
                filters.team,
            )
            componentsApi = response.components
            filtersResponse = response.filters
        }
    }

    fun refreshSearch(filters: FiltersSelected) = navigate.invoke(buildUrlWithParameters(apiReportPath, filters))

    useEffect {
        if (currentFilters != queryFilters) loadApiReport(queryFilters)
    }

    Grid {
        container = true
        direction = responsive(GridDirection.row)
        columnSpacing = responsive(Themes.SPACE_PADDING)
        sx = jso { height = 100.pct }

        Grid {
            item = true
            xs = 4; md = 3; xl = 2

            Paper {
                sx = jso { padding = Themes.SPACE_PADDING; height = 100.pct }
                elevation = 2

                Typography { variant = TypographyVariant.h5; +"Filters" }
                Spacer { height = 10.px }
                LoadingBar { isLoading = filtersResponse == null }
                filtersResponse?.let {
                    ReportFilters {
                        filtersSelected = currentFilters ?: FiltersSelected()
                        filters = it
                        onFilterChanged = { filters -> refreshSearch(filters) }
                        searchTextFilterLabel = "Path"
                        hideStatusFilter = true
                        hideUpdatedTimeAfter = true
                    }
                }
            }
        }

        Grid {
            item = true
            xs = 8; md = 9; xl = 10

            Paper {
                sx = jso { padding = Themes.SPACE_PADDING; height = 100.pct }
                elevation = 2
                Typography { variant = TypographyVariant.h5; +"Services API" }
                Spacer { height = 10.px }
                LoadingBar { isLoading = componentsApi == null }
                componentsApi?.let { ComponentsApiReport { components = it; expanded = currentFilters?.isNotEmpty() } }
            }
        }
    }
}
