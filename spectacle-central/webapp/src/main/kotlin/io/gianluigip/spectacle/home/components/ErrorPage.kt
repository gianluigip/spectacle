package io.gianluigip.spectacle.home.components

import js.errors.JsError
import mui.material.Typography
import mui.material.styles.TypographyVariant
import react.FC
import react.Props
import react.router.useRouteError

val ErrorPage = FC<Props> {
    val error = useRouteError().unsafeCast<JsError>()

    val message = if (error != undefined) {
        error.message
    } else "Page Not Found"

    Typography {
        variant = TypographyVariant.h3
        +message
    }
}
