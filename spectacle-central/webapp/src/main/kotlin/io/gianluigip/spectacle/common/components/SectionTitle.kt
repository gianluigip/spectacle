package io.gianluigip.spectacle.common.components

import csstype.Color
import csstype.rem
import io.gianluigip.spectacle.home.ThemeContext
import kotlinx.js.jso
import mui.material.Typography
import mui.material.styles.TypographyVariant
import react.FC
import react.Props
import react.useContext

external interface SectionTitleProps : Props {
    var text: String
    var color: Color?
}

val SectionTitle = FC<SectionTitleProps> { props ->
    val theme by useContext(ThemeContext)
    val textColor = props.color ?: theme.palette.info.main
    Typography {
        variant = TypographyVariant.h6
        sx = jso { color = textColor; fontSize = 1.1.rem }
        +props.text
    }
}
