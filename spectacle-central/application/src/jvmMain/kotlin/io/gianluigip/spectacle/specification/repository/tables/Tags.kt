package io.gianluigip.spectacle.specification.repository.tables

import io.gianluigip.spectacle.common.repository.StringIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import io.gianluigip.spectacle.specification.repository.tables.Specifications as Specs

object Tags : StringIdTable(name = "tags") {
    val creationTime = datetime("creation_time")
    val updateTime = datetime("update_time")
    val specId = varchar("spec_id", 100).references(Specs.id)
    val name = varchar("name", 255)
    val teamName = varchar("team_name", 255)
    val tagSource = varchar("source", 255)
    val component = varchar("component", 255)
}
