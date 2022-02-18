package io.gianluigip.spectacle.diagram

import csstype.FlexGrow
import io.gianluigip.spectacle.common.component.Diagram
import io.gianluigip.spectacle.common.utils.buildReportUrlWithParameters
import io.gianluigip.spectacle.common.utils.escapeSpaces
import io.gianluigip.spectacle.common.utils.parseParams
import io.gianluigip.spectacle.diagram.api.getInteractionsReport
import io.gianluigip.spectacle.report.api.model.ReportFiltersResponse
import io.gianluigip.spectacle.report.api.model.SystemInteractionResponse
import io.gianluigip.spectacle.specification.component.FiltersSelected
import io.gianluigip.spectacle.specification.model.InteractionDirection
import io.gianluigip.spectacle.specification.model.InteractionDirection.INBOUND
import io.gianluigip.spectacle.specification.model.InteractionDirection.OUTBOUND
import io.gianluigip.spectacle.specification.model.InteractionType
import io.gianluigip.spectacle.specification.model.InteractionType.EVENT
import io.gianluigip.spectacle.specification.model.InteractionType.HTTP
import io.gianluigip.spectacle.specification.model.InteractionType.PERSISTENCE
import io.gianluigip.spectacle.specification.model.SpecStatus
import kotlinext.js.jso
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.LinearProgress
import mui.material.TextField
import mui.material.Typography
import react.FC
import react.Props
import react.dom.onChange
import react.router.useLocation
import react.router.useNavigate
import react.useEffect
import react.useState

const val systemDiagramPath = "/system_diagram"
val SystemDiagram = FC<Props> {
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
        status = queryParams["status"]?.let { status -> SpecStatus.values().firstOrNull { it.name == status } },
    )

    var testValue by useState("DSL")

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
        variant = "h3"
        +"System Diagram"
    }

    TextField {
        value = testValue
        onChange = { event -> testValue = event.target.asDynamic().value ?: "" }
    }

    if (interactions == null) {
        LinearProgress { sx = jso { FlexGrow(1.0) } }
    }
    interactions?.let { interactions ->
        println("""
        graph LR
        ${generateComponents(interactions)} 
        ${generateDiagramInteractions(interactions)}
    """.trimIndent())

        Diagram {
            content = """
            graph LR
            sourceCode("Source Code")
            spectacleDsl("Spectacle $testValue")
            spectacleCentral("Spectacle Central")
            
            sourceCode --> spectacleDsl 
            spectacleDsl --> spectacleCentral 
        """.trimIndent()
        }
    }
}

private fun generateComponents(interactions: List<SystemInteractionResponse>): String {
    val components = mutableSetOf<String>()
    interactions.forEach { interaction ->
        components += "${interaction.component.toDiagramId()}(${interaction.component})"
        when (interaction.type) {
            EVENT -> components += """${interaction.interactionName.toDiagramId()}{{"${interaction.interactionName}"}}"""
            PERSISTENCE -> components += """${interaction.interactionName.toDiagramId()}[("${interaction.interactionName}")]"""
            HTTP -> "${interaction.interactionName.toDiagramId()}(${interaction.interactionName})"
        }
    }
    return components.joinToString("\n")
}

private fun generateDiagramInteractions(interactions: List<SystemInteractionResponse>): String {
    val mermaidInteractions = mutableSetOf<String>()
    interactions.forEach {
        val mermaidInteraction = when (it.direction) {
            INBOUND -> "${it.interactionName.toDiagramId()} --> ${it.component.toDiagramId()}"
            OUTBOUND -> "${it.component.toDiagramId()} --> ${it.interactionName.toDiagramId()}"
        }
        mermaidInteractions += mermaidInteraction
    }
    return mermaidInteractions.joinToString("\n")
}

private fun String.toDiagramId() = replace(" ", "")
