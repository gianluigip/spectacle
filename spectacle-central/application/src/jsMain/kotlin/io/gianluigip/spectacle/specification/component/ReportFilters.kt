package io.gianluigip.spectacle.specification.component

import io.gianluigip.spectacle.report.api.model.ReportFiltersResponse
import io.gianluigip.spectacle.specification.model.SpecStatus
import mui.material.Stack
import mui.system.ResponsiveStyleValue
import react.FC
import react.Props

data class FiltersSelected(
    val feature: String? = null,
    val source: String? = null,
    val component: String? = null,
    val tag: String? = null,
    val team: String? = null,
    val status: SpecStatus? = null,
)

external interface ReportFilersProps : Props {
    var filters: ReportFiltersResponse
    var filtersSelected: FiltersSelected
    var onFilterChanged: (FiltersSelected) -> Unit
}

val ReportFilters = FC<ReportFilersProps> {
    val filters = it.filters
    val selected = it.filtersSelected

    Stack {
        spacing = ResponsiveStyleValue(2)
        ReportFilter {
            label = "Features"
            value = selected.feature
            options = filters.features
            onFilterChanged = { newValue -> it.onFilterChanged.invoke(selected.copy(feature = newValue)) }
        }
        ReportFilter {
            label = "Tags"
            value = selected.tag
            options = filters.tags
            onFilterChanged = { newValue -> it.onFilterChanged.invoke(selected.copy(tag = newValue)) }
        }
        ReportFilter {
            label = "Teams"
            value = selected.team
            options = filters.teams
            onFilterChanged = { newValue -> it.onFilterChanged.invoke(selected.copy(team = newValue)) }
        }
        ReportFilter {
            label = "Statuses"
            value = selected.status?.display
            options = filters.statuses.map { it.display }
            onFilterChanged = { newValue ->
                it.onFilterChanged.invoke(selected.copy(status = newValue?.let { status -> SpecStatus.fromDisplay(status) }))
            }
        }
        ReportFilter {
            label = "Components"
            value = selected.component
            options = filters.components
            onFilterChanged = { newValue -> it.onFilterChanged.invoke(selected.copy(component = newValue)) }
        }
        ReportFilter {
            label = "Sources"
            value = selected.source
            options = filters.sources
            onFilterChanged = { newValue -> it.onFilterChanged.invoke(selected.copy(source = newValue)) }
        }
    }
}
