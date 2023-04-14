package io.gianluigip.spectacle.feature.components

import csstype.Color
import csstype.None
import csstype.px
import emotion.react.css
import io.gianluigip.spectacle.common.components.LoadingBar
import io.gianluigip.spectacle.common.components.MetaDataChip
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.toDisplay
import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.feature.api.getFeatures
import io.gianluigip.spectacle.feature.api.model.FeatureResponse
import io.gianluigip.spectacle.home.ThemeContext
import js.core.jso
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.*
import mui.material.GridDirection.Companion.column
import mui.material.GridDirection.Companion.row
import mui.material.styles.Theme
import mui.material.styles.TypographyVariant.Companion.h6
import mui.system.responsive
import react.*
import react.router.dom.NavLink

external interface FeaturesListProps : Props {
    var showMetadata: Boolean?
}

val FeaturesList = FC<FeaturesListProps> {
    val theme by useRequiredContext(ThemeContext)
    var showMetadata by useState(it.showMetadata ?: false)

    var features by useState<List<FeatureResponse>>()
    useEffectOnce {
        MainScope().launch {
            features = getFeatures()
        }
    }

    LoadingBar { isLoading = features == null }
    features?.let {

        Grid {
            container = true
            direction = responsive(column)
            features!!.forEachIndexed { i, feature ->
                +generateFeatureComponent(feature, isFirstFeature = i == 0, showMetadata, theme)
            }
        }
    }

    FormGroup {
        FormControlLabel {
            label = "Show Metadata".toNode()
            control = Switch.create {
                checked = showMetadata
                onChange = { _, newValue -> showMetadata = newValue }
            }
        }
    }
}

private fun generateFeatureComponent(feature: FeatureResponse, isFirstFeature: Boolean, showMetadata: Boolean, theme: Theme): ReactNode =
    Box.create {
        if (!isFirstFeature) Spacer { height = 10.px }
        NavLink {
            to = buildFeatureUrl(feature.name)
            css { textDecoration = None.none; color = Color.currentcolor }
            Tooltip {
                title = "Go to Feature".toNode()
                followCursor = true
                Typography { sx = jso { color = theme.palette.info.main }; variant = h6; +feature.name }
            }
        }
        if (feature.description.isNotEmpty()) Typography { +feature.description }
        Grid {
            container = true
            spacing = responsive(1)
            direction = responsive(row)
            if (!showMetadata) sx = jso { display = None.none }
            Grid { item = true; MetaDataChip { label = "Components: ${feature.components.sorted().joinToString(", ")}" } }
            Grid { item = true; MetaDataChip { label = "Created: ${feature.creationTime.toDisplay()}" } }
            Grid { item = true; MetaDataChip { label = "Updated: ${feature.updateTime.toDisplay()}" } }
        }
    }
