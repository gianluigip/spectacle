package io.gianluigip.spectacle.feature.components

import csstype.pct
import csstype.px
import io.gianluigip.spectacle.common.components.LoadingBar
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.escapeSpaces
import io.gianluigip.spectacle.common.utils.parseParams
import io.gianluigip.spectacle.diagram.api.getInteractionsReport
import io.gianluigip.spectacle.feature.api.getFeatures
import io.gianluigip.spectacle.feature.api.model.FeatureResponse
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.home.Themes
import io.gianluigip.spectacle.report.api.model.InteractionsReportResponse
import io.gianluigip.spectacle.report.api.model.SpecsReportResponse
import io.gianluigip.spectacle.specification.api.getSpecReport
import io.gianluigip.spectacle.wiki.api.getAllPages
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.js.jso
import mui.material.Paper
import mui.material.Typography
import react.FC
import react.Props
import react.router.useLocation
import react.router.useNavigate
import react.useContext
import react.useEffect
import react.useEffectOnce
import react.useState

const val featuresPath = "/features"

val FeaturesPage = FC<Props> { _ ->
    val theme by useContext(ThemeContext)
    val navigate = useNavigate()
    val queryParams = useLocation().search.parseParams()

    var features by useState<List<FeatureResponse>>()
    var currentFeatureName by useState("")
    val featureName = queryParams["name"]
    val selectedFeature = features?.firstOrNull { it.name == featureName }

    var specs by useState<SpecsReportResponse>()
    var interactions by useState<InteractionsReportResponse>()
    var wikiPages by useState<List<WikiPageMetadataResponse>>()

    fun loadFeature(featureName: String) {
        specs = null
        interactions = null
        wikiPages = null
        currentFeatureName = featureName
        MainScope().launch {
            selectedFeature?.let {
                specs = getSpecReport(feature = it.name)
                interactions = getInteractionsReport(feature = it.name)
                wikiPages = getAllPages(feature = it.name)
            }
        }
    }

    useEffectOnce { MainScope().launch { features = getFeatures() } }
    useEffect { if (features != null && featureName != null && currentFeatureName != featureName) loadFeature(featureName) }

    Paper {
        sx = jso { padding = Themes.SPACE_PADDING; height = 100.pct }
        elevation = 2

        LoadingBar { isLoading = features == null }
        features?.let { features ->
            Typography { variant = "h6"; +"Select a Feature" }
            Spacer { height = 10.px }
            FeaturesSelector {
                this.features = features
                this.selected = selectedFeature
                onSelect = { selected ->
                    navigate.invoke(buildFeatureUrl(selected?.name))
                }
            }
        }

        Spacer { height = 10.px }
        if (featureName != null && selectedFeature != null && specs != null && interactions != null && wikiPages != null) {
            FeatureViewer {
                this.feature = selectedFeature
                this.specs = specs!!
                this.interactions = interactions!!
                this.wikiPages = wikiPages!!
            }
        } else {
            LoadingBar { isLoading = featureName != null }
        }
    }

}

fun buildFeatureUrl(featureName: String?): String = if (featureName != null) {
    "$featuresPath?name=${featureName.escapeSpaces()}"
} else featuresPath
