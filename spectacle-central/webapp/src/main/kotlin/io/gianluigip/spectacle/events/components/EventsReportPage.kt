package io.gianluigip.spectacle.events.components

import csstype.pct
import csstype.px
import io.gianluigip.spectacle.common.components.*
import io.gianluigip.spectacle.common.utils.buildUrlWithParameters
import io.gianluigip.spectacle.common.utils.parseParams
import io.gianluigip.spectacle.events.api.getEventReport
import io.gianluigip.spectacle.home.Themes
import io.gianluigip.spectacle.navigation.logic.Paths.eventsPath
import io.gianluigip.spectacle.report.api.model.EventReportResponse
import io.gianluigip.spectacle.report.api.model.ReportFiltersResponse
import io.gianluigip.spectacle.specification.components.FiltersSelected
import io.gianluigip.spectacle.specification.components.ReportFilters
import js.core.jso
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
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

val EventsReportPage = FC<Props> {
    val navigate = useNavigate()

    var eventsResponse by useState<List<EventReportResponse>>()
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

    fun loadEventReport(filters: FiltersSelected) {
        eventsResponse = null
        currentFilters = queryFilters
        MainScope().launch {
            val response = getEventReport(
                eventName = filters.searchText,
                feature = filters.feature,
                source = filters.source,
                component = filters.component,
                tag = filters.tag,
                team = filters.team,
            )
            eventsResponse = response.events
            filtersResponse = response.filters
        }
    }

    fun refreshSearch(filters: FiltersSelected) = navigate.invoke(buildUrlWithParameters(eventsPath, filters))

    useEffect {
        if (currentFilters != queryFilters) loadEventReport(queryFilters)
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
                        searchTextFilterLabel = "Event"
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
                Typography { variant = TypographyVariant.h5; +"System Events" }
                Spacer { height = 10.px }
                LoadingBar { isLoading = eventsResponse == null }
                eventsResponse?.forEach { eventResponse ->
                    EventAccordion { event = eventResponse }
                }
            }
        }
    }
}
