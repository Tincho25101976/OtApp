package com.vsg.ot.common.model.securityDialog.xact.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecord
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSector

data class XactSectorRecord(
    @Embedded
    val parent: XactSector,
    @Relation(
        parentColumn = "id",
        entityColumn = "idSector"
    )
    val children: List<XactRecord>
)
