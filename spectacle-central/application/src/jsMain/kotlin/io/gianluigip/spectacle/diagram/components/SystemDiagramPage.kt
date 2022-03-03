package io.gianluigip.spectacle.diagram.components

import csstype.Auto
import csstype.Display
import csstype.pct
import csstype.px
import io.gianluigip.spectacle.common.components.LoadingBar
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.buildUrlWithParameters
import io.gianluigip.spectacle.common.utils.parseParams
import io.gianluigip.spectacle.diagram.api.getInteractionsReport
import io.gianluigip.spectacle.home.Themes.SPACE_PADDING
import io.gianluigip.spectacle.report.api.model.ReportFiltersResponse
import io.gianluigip.spectacle.report.api.model.SystemInteractionResponse
import io.gianluigip.spectacle.specification.components.FiltersSelected
import io.gianluigip.spectacle.specification.components.ReportFilters
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.js.jso
import mui.material.Box
import mui.material.Paper
import mui.material.Typography
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

    fun refreshSearch(filters: FiltersSelected) = navigate.invoke(buildUrlWithParameters(systemDiagramPath, filters))

    useEffect {
        if (currentFilters != queryFilters) loadInteractionsReport(queryFilters)
    }

    Box {
        sx = jso { display = Display.flex; height = 100.pct; width = 100.pct }

        Box {
            sx = jso { height = 100.pct; }
            Paper {
                sx = jso { padding = SPACE_PADDING; height = 100.pct; width = 100.pct }
                elevation = 2

                Typography { sx = jso { width = 280.px; }; variant = "h5"; +"Filters" }
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

        Box {
            sx = jso { width = 100.pct; height = 100.pct; paddingLeft = SPACE_PADDING; }

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
