package io.gianluigip.spectacle.navigation

import io.gianluigip.spectacle.common.utils.buildUrlWithParameters
import io.gianluigip.spectacle.navigation.logic.Paths.specificationsPath
import io.gianluigip.spectacle.specification.components.FiltersSelected
import kotlinx.browser.window

private val host = window.location.origin

fun generateSpecificationReportExternalLink(filters: FiltersSelected): String {
    return buildUrlWithParameters("${host}/#$specificationsPath", filters)
}
