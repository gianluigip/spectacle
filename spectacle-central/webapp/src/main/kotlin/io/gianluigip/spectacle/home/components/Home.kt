package io.gianluigip.spectacle.home.components

import csstype.*
import io.gianluigip.spectacle.auth.AuthModule
import io.gianluigip.spectacle.home.GridAreas
import io.gianluigip.spectacle.home.Sizes
import io.gianluigip.spectacle.navigation.components.Header
import js.core.jso
import mui.system.Box
import react.FC
import react.Props

val Home = FC<Props> {

    AuthModule {
        Box {
            sx = jso {
                display = Display.grid
                height = 100.pct
                gridTemplateRows = "${Sizes.Header.Height} ${Auto.auto}".unsafeCast<GridTemplateRows>()
                gridTemplateColumns = Auto.auto
                gridTemplateAreas = GridTemplateAreas(
                    GridAreas.Header,
                    GridAreas.Content,
                )
            }

            Header()
            Content()
        }
    }
}
