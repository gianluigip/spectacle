package io.gianluigip.spectacle.report.config

const val CENTRAL_ENABLE_ENV_VARIABLE = "SPECIFICATION_PUBLISHER_CENTRAL_ENABLED"
const val CENTRAL_HOST_ENV_VARIABLE = "SPECIFICATION_PUBLISHER_CENTRAL_HOST"

expect object ConfigLoader {

    val CONFIG: ReportConfiguration

}
