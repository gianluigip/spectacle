package io.gianluigip.spectacle.component.components

import csstype.pct
import csstype.px
import io.gianluigip.spectacle.api.api.getApiReport
import io.gianluigip.spectacle.common.components.LoadingBar
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.escapeSpaces
import io.gianluigip.spectacle.common.utils.parseParams
import io.gianluigip.spectacle.component.api.Component
import io.gianluigip.spectacle.component.api.getComponents
import io.gianluigip.spectacle.diagram.api.getInteractionsReport
import io.gianluigip.spectacle.events.api.getEventReport
import io.gianluigip.spectacle.home.Themes.SPACE_PADDING
import io.gianluigip.spectacle.navigation.logic.Paths.componentsPath
import io.gianluigip.spectacle.report.api.model.ApiReportResponse
import io.gianluigip.spectacle.report.api.model.EventsReportResponse
import io.gianluigip.spectacle.report.api.model.InteractionsReportResponse
import io.gianluigip.spectacle.report.api.model.SpecsReportResponse
import io.gianluigip.spectacle.specification.api.getSpecReport
import io.gianluigip.spectacle.wiki.api.getAllPages
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import js.core.jso
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import mui.material.Paper
import mui.material.Typography
import mui.material.styles.TypographyVariant.Companion.h6
import react.*
import react.router.useLocation
import react.router.useNavigate

val ComponentsPage = FC<Props> { _ ->
    val navigate = useNavigate()
    val queryParams = useLocation().search.parseParams()

    var components by useState<List<Component>>()
    var currentComponent by useState("")
    val componentName = queryParams["name"]
    val selectedComponent = components?.firstOrNull { it.name == componentName }

    var interactions by useState<InteractionsReportResponse>()
    var events by useState<EventsReportResponse>()
    var api by useState<ApiReportResponse>()
    var features by useState<SpecsReportResponse>()
    var wikiPages by useState<List<WikiPageMetadataResponse>>()

    fun loadComponent(componentName: String) {
        interactions = null
        events = null
        api = null
        features = null
        wikiPages = null
        currentComponent = componentName
        MainScope().launch {
            selectedComponent?.let {
                awaitAll(
                    async { interactions = getInteractionsReport(component = it.name) },
                    async { events = getEventReport(component = it.name) },
                    async { api = getApiReport(component = it.name) },
                    async { features = getSpecReport(component = it.name) },
                    async { wikiPages = getAllPages(component = it.name) },
                )
            }
        }
    }

    useEffectOnce { MainScope().launch { components = getComponents() } }
    useEffect { if (components != null && componentName != null && currentComponent != componentName) loadComponent(componentName) }

    Paper {
        sx = jso { padding = SPACE_PADDING; height = 100.pct }
        elevation = 2

        LoadingBar { isLoading = components == null }
        components?.let { components ->
            Typography { variant = h6; +"Select a Component" }
            Spacer { height = 10.px }
            ComponentSelector {
                this.components = components
                this.selected = selectedComponent
                onSelect = { selected ->
                    navigate.invoke(buildComponentUrl(selected?.name))
                }
            }
        }

        Spacer { height = 10.px }
        if (componentName != null && selectedComponent != null &&
            events != null && api != null && features != null && interactions != null && wikiPages != null
        ) {
            ComponentViewer {
                this.component = selectedComponent
                this.interactions = interactions!!
                this.events = events!!
                this.api = api!!
                this.features = features!!
                this.wikiPages = wikiPages!!
            }
        } else {
            LoadingBar { isLoading = componentName != null }
        }
    }
}

fun buildComponentUrl(componentName: String?): String = if (componentName != null) {
    "$componentsPath?name=${componentName.escapeSpaces()}"
} else componentsPath
