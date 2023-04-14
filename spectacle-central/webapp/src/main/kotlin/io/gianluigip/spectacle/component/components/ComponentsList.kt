package io.gianluigip.spectacle.component.components

import csstype.Color
import csstype.None
import csstype.px
import emotion.react.css
import io.gianluigip.spectacle.common.components.LoadingBar
import io.gianluigip.spectacle.common.components.MetaDataChip
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.escapeSpaces
import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.component.api.Component
import io.gianluigip.spectacle.component.api.getComponents
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.navigation.logic.Paths.componentsPath
import js.core.jso
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.Box
import mui.material.Grid
import mui.material.GridDirection.Companion.column
import mui.material.GridDirection.Companion.row
import mui.material.Tooltip
import mui.material.Typography
import mui.material.styles.Theme
import mui.material.styles.TypographyVariant.Companion.h6
import mui.system.responsive
import react.*
import react.router.dom.NavLink

val ComponentList = FC<Props> {
    val theme by useRequiredContext(ThemeContext)

    var components by useState<List<Component>>()
    useEffectOnce {
        MainScope().launch {
            components = getComponents()
        }
    }

    LoadingBar { isLoading = components == null }
    components?.let {

        Grid {
            container = true
            direction = responsive(column)
            components!!.forEachIndexed { i, feature ->
                +generateComponentsBox(feature, isFirstFeature = i == 0, theme)
            }
        }
    }
}

private fun generateComponentsBox(component: Component, isFirstFeature: Boolean, theme: Theme): ReactNode =
    Box.create {
        if (!isFirstFeature) Spacer { height = 10.px }
        NavLink {
            to = "$componentsPath?name=${component.name.escapeSpaces()}"
            css { textDecoration = None.none; color = Color.currentcolor }
            Tooltip {
                title = "Go to Component Diagram".toNode()
                followCursor = true
                Typography { sx = jso { color = theme.palette.info.main }; variant = h6; +component.name }
            }
        }
        Grid {
            container = true
            spacing = responsive(1)
            direction = responsive(row)

            Grid { item = true; MetaDataChip { label = "Teams: ${component.teams.sorted().joinToString(", ")}" } }
        }
    }
