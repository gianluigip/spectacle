package io.gianluigip.spectacle.report.api.model

import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.SpecificationStep
import kotlinx.serialization.Serializable

@Serializable
data class SpecsReportResponse(
    val features: List<FeatureReportResponse>,
    val filters: ReportFiltersResponse,
)

@Serializable
data class FeatureReportResponse(
    val name: String,
    val description: String,
    val specs: List<SpecReportResponse>
)

@Serializable
data class SpecReportResponse(
    val name: String,
    val team: String,
    val source: String,
    val component: String,
    val status: SpecStatus,
//    val creationTime: ZonedDateTime,
//    val updateTime: ZonedDateTime,
    val tags: List<String>,
    val steps: List<SpecificationStep>
)

@Serializable
data class ReportFiltersResponse(
    val features: Set<String>,
    val sources: Set<String>,
    val components: Set<String>,
    val tags: Set<String>,
    val teams: Set<String>,
    val statuses: Set<SpecStatus>,
)