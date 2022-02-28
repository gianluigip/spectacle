package io.gianluigip.spectacle.common.components

import io.gianluigip.spectacle.common.utils.toNode
import mui.material.Chip
import mui.material.ChipColor
import mui.material.ChipVariant
import react.FC
import react.Props

external interface SpecMetaDataChipProps : Props {
    var label: String
}

val MetaDataChip = FC<SpecMetaDataChipProps> {
    Chip { variant = ChipVariant.outlined; color = ChipColor.info; label = it.label.toNode() }
}
