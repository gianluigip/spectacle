package io.gianluigip.spectacle.specification.components

import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.report.api.model.ReportFiltersResponse
import io.gianluigip.spectacle.specification.model.SpecStatus
import kotlinx.datetime.Instant
import mui.material.Stack
import mui.material.TextField
import mui.system.responsive
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.onChange

data class FiltersSelected(
    val searchText: String? = null,
    val feature: String? = null,
    val source: String? = null,
    val component: String? = null,
    val tag: String? = null,
    val team: String? = null,
    val status: SpecStatus? = null,
    val updatedTimeAfter: Instant? = null,
)

external interface ReportFilersProps : Props {
    var filters: ReportFiltersResponse
    var filtersSelected: FiltersSelected
    var hideSearchTextFilter: Boolean?
    var hideUpdatedTimeAfter: Boolean?
    var hideStatusFilter: Boolean?
    var onFilterChanged: (FiltersSelected) -> Unit
}

val ReportFilters = FC<ReportFilersProps> { props ->
    val filters = props.filters
    val selected = props.filtersSelected

    Stack {
        spacing = responsive(2)
        TextField {
            label = "Keywords".toNode()
            value = selected.searchText
            type = InputType.search
            onChange = { newValue -> props.onFilterChanged.invoke(selected.copy(searchText = newValue.target.asDynamic().value as String)) }
        }
        ReportFilter {
            label = "Features"
            value = selected.feature
            options = filters.features
            onFilterChanged = { newValue -> props.onFilterChanged.invoke(selected.copy(feature = newValue)) }
        }
        ReportFilter {
            label = "Tags"
            value = selected.tag
            options = filters.tags
            onFilterChanged = { newValue -> props.onFilterChanged.invoke(selected.copy(tag = newValue)) }
        }
        ReportFilter {
            label = "Teams"
            value = selected.team
            options = filters.teams
            onFilterChanged = { newValue -> props.onFilterChanged.invoke(selected.copy(team = newValue)) }
        }
        if (props.hideStatusFilter != true) {
            ReportFilter {
                label = "Statuses"
                value = selected.status?.display
                options = filters.statuses.map { it.display }
                onFilterChanged = { newValue ->
                    props.onFilterChanged.invoke(selected.copy(status = newValue?.let { status -> SpecStatus.fromDisplay(status) }))
                }
            }
        }
        ReportFilter {
            label = "Components"
            value = selected.component
            options = filters.components
            onFilterChanged = { newValue -> props.onFilterChanged.invoke(selected.copy(component = newValue)) }
        }
        ReportFilter {
            label = "Sources"
            value = selected.source
            options = filters.sources
            onFilterChanged = { newValue -> props.onFilterChanged.invoke(selected.copy(source = newValue)) }
        }
        ReportTimeFilter {
            label = "Updated After"
            value = selected.updatedTimeAfter
            onFilterChanged = { newValue -> props.onFilterChanged.invoke(selected.copy(updatedTimeAfter = newValue)) }
        }
    }
}
