package io.gianluigip.spectacle.specification.repository.tables

import io.gianluigip.spectacle.common.repository.StringIdTable
import io.gianluigip.spectacle.team.model.TeamToDelete
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.javatime.datetime

fun TeamToDelete.searchExpression(): Op<Boolean> = Op.build {
    (Teams.name eq name.value) and (Teams.teamSource eq source.value)
}

object Teams : StringIdTable(name = "teams") {
    val creationTime = datetime("creation_time")
    val updateTime = datetime("update_time")
    val name = varchar("name", 500)
    val teamSource = varchar("source", 500)
    val component = varchar("component", 500)
}
