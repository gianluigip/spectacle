package io.gianluigip.spectacle.diagram.components

import csstype.FlexGrow
import io.gianluigip.spectacle.common.utils.buildReportUrlWithParameters
import io.gianluigip.spectacle.common.utils.parseParams
import io.gianluigip.spectacle.diagram.api.getInteractionsReport
import io.gianluigip.spectacle.report.api.model.ReportFiltersResponse
import io.gianluigip.spectacle.report.api.model.SystemInteractionResponse
import io.gianluigip.spectacle.specification.component.FiltersSelected
import kotlinext.js.jso
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.LinearProgress
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
    }

    fun refreshSearch(filters: FiltersSelected) = navigate.invoke(buildReportUrlWithParameters(systemDiagramPath, filters))

    useEffect {
        if (currentFilters != queryFilters) {
            currentFilters = queryFilters
            loadInteractionsReport(queryFilters)
        }
    }

    Typography {
        variant = "h5"
        +"System Diagram"
    }

    if (interactions == null) {
        LinearProgress { sx = jso { FlexGrow(1.0) } }
    }
    interactions?.let { interactions ->
        SystemDiagram { this.interactions = interactions }
    }
}
