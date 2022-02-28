package io.gianluigip.spectacle.diagram.components

import csstype.Auto
import csstype.pct
import csstype.px
import io.gianluigip.spectacle.common.component.LoadingBar
import io.gianluigip.spectacle.common.component.Spacer
import io.gianluigip.spectacle.common.utils.buildReportUrlWithParameters
import io.gianluigip.spectacle.common.utils.parseParams
import io.gianluigip.spectacle.diagram.api.getInteractionsReport
import io.gianluigip.spectacle.home.Themes.SPACE_PADDING
import io.gianluigip.spectacle.report.api.model.ReportFiltersResponse
import io.gianluigip.spectacle.report.api.model.SystemInteractionResponse
import io.gianluigip.spectacle.specification.component.FiltersSelected
import io.gianluigip.spectacle.specification.component.ReportFilters
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.js.jso
import mui.material.Grid
import mui.material.GridDirection
import mui.material.Paper
import mui.material.Typography
import mui.system.ResponsiveStyleValue
import react.FC
import react.Props
import react.router.useLocation
import react.router.useNavigate
import react.useEffect
import react.useState

const val systemDiagramPath = "/system_diagram"
val SystemDiagramPage = FC<Props> {
    val navigate = useNavigate()

    var interactions by useState<List<SystemInteractionResponse>>()
    var filters by useState<ReportFiltersResponse>()
    var currentFilters by useState<FiltersSelected>()
    val queryParams = useLocation().search.parseParams()
    val queryFilters = FiltersSelected(
        feature = queryParams["feature"],
        source = queryParams["source"],
        component = queryParams["component"],
        tag = queryParams["tag"],
        team = queryParams["team"],
    )

    fun loadInteractionsReport(filtersSelected: FiltersSelected) = MainScope().launch {
        val response = filtersSelected.run { getInteractionsReport(feature, source, component, tag, team) }
        interactions = response.interactions
        filters = response.filters
        currentFilters = queryFilters
    }

    fun refreshSearch(filters: FiltersSelected) = navigate.invoke(buildReportUrlWithParameters(systemDiagramPath, filters))

    useEffect {
        if (currentFilters != queryFilters) {
            loadInteractionsReport(queryFilters)
        }
    }

    Grid {
        container = true
        sx = jso { height = 100.pct }
        direction = ResponsiveStyleValue(GridDirection.row)

        Grid {
            item = true
            sx = jso { maxWidth = 300.px; }
            xs = 4; md = 3; xl = 2

            Paper {
                sx = jso { padding = SPACE_PADDING; height = 100.pct }
                elevation = 2

                Typography { variant = "h5"; +"Filters" }
                Spacer { height = 10.px }
                LoadingBar { isLoading = currentFilters != queryFilters }
                filters?.let {
                    ReportFilters {
                        filtersSelected = currentFilters ?: FiltersSelected()
                        this.filters = it
                        onFilterChanged = { filters -> refreshSearch(filters) }
                        hideStatusFilter = true
                    }
                }
            }
        }
        Grid {
            item = true
            sx = jso { paddingLeft = SPACE_PADDING; }
            xs = 8; md = 9; xl = 10


            Paper {
                sx = jso { padding = SPACE_PADDING; height = 100.pct; width = 100.pct; overflow = Auto.auto }
                elevation = 2

                Typography { variant = "h5"; +"System Diagram" }
                Spacer { height = 10.px }
                LoadingBar { isLoading = currentFilters != queryFilters }
                interactions?.let {
                    if (it.isEmpty()) return@let
                    SystemDiagram {
                        this.interactions = it
                        this.components = filters?.components ?: emptySet()
                    }
                }
            }
        }
    }
}
