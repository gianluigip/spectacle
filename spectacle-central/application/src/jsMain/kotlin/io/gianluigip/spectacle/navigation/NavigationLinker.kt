package io.gianluigip.spectacle.navigation

import io.gianluigip.spectacle.common.utils.buildReportUrlWithParameters
import io.gianluigip.spectacle.specification.component.FiltersSelected
import io.gianluigip.spectacle.specification.component.specificationsReportPath
import kotlinx.browser.window

private val host = window.location.origin

fun generateSpecificationReportExternalLink(filters: FiltersSelected): String {
    return buildReportUrlWithParameters("${host}/#$specificationsReportPath", filters)
}