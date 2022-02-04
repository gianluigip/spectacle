package io.gianluigip.spectacle.specification.repository.tables

import io.gianluigip.spectacle.common.repository.StringIdTable
import io.gianluigip.spectacle.specification.model.Specification
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.javatime.datetime
import io.gianluigip.spectacle.specification.repository.tables.Specifications as Specs

fun Specification.searchExpression() = Op.build {
    (Specs.specSource eq source.value) and (Specs.name eq name.value)
}

object Specifications : StringIdTable(name = "specifications") {
    val creationTime = datetime("creation_time")
    val updateTime = datetime("update_time")
    val name = varchar("name", 1000)
    val feature = varchar("feature", 1000)
    val team = varchar("team", 255)
    val specSource = varchar("source", 255)
    val status = varchar("status", 255)
    override val primaryKey = PrimaryKey(id)
}

object SpecificationSteps : StringIdTable(name = "specification_steps") {
    val creationTime = datetime("creation_time")
    val updateTime = datetime("update_time")
    val specId = varchar("spec_id", 100).references(Specs.id)
    val type = varchar("type", 255)
    val description = varchar("description", 1000)
    val index = integer("spec_index")
    override val primaryKey = PrimaryKey(id)
}
