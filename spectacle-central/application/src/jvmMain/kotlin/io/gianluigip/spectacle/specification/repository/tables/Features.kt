package io.gianluigip.spectacle.specification.repository.tables

import io.gianluigip.spectacle.common.repository.StringIdTable
import io.gianluigip.spectacle.specification.model.FeatureToDelete
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.javatime.datetime

fun FeatureToDelete.searchExpression(): Op<Boolean> = Op.build {
    (Features.name eq name.value) and (Features.featureSource eq source.value)
}

object Features : StringIdTable(name = "features") {
    val creationTime = datetime("creation_time")
    val updateTime = datetime("update_time")
    val name = varchar("name", 1000)
    val description = text("description")
    val featureSource = varchar("source", 255)
    val component = varchar("component", 255)
}
