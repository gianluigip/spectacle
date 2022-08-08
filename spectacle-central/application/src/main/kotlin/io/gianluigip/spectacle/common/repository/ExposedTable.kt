package io.gianluigip.spectacle.common.repository

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

open class StringIdTable(name: String = "", columnName: String = "id") : IdTable<String>(name) {
    override val id: Column<EntityID<String>> = varchar(columnName, 100).entityId()
    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }
}
