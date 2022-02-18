package io.gianluigip.spectacle.specification.repository.tables

import io.gianluigip.spectacle.common.repository.StringIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import io.gianluigip.spectacle.specification.repository.tables.Specifications as Specs

object SpecificationInteractions : StringIdTable(name = "specification_interactions") {
    val creationTime = datetime("creation_time")
    val updateTime = datetime("update_time")
    val specId = varchar("spec_id", 100).references(Specs.id)
    val interactionSource = varchar("source", 255)
    val component = varchar("component", 255)
    val direction = varchar("direction", 255)
    val type = varchar("type", 255)
    val name = varchar("name", 1000)
    val metadata = text("metadata")
}