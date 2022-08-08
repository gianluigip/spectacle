package io.gianluigip.spectacle.navigation

import io.gianluigip.spectacle.common.utils.buildUrlWithParameters
import io.gianluigip.spectacle.specification.components.FiltersSelected
import io.gianluigip.spectacle.specification.components.specificationsReportPath
import kotlinx.browser.window

private val host = window.location.origin

fun generateSpecificationReportExternalLink(filters: FiltersSelected): String {
    return buildUrlWithParameters("${host}/#$specificationsReportPath", filters)
}