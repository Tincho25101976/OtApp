package com.vsg.ot.common.model.securityDialog.xact.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.vsg.ot.common.model.securityDialog.xact.event.XactEvent
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecord

data class XactEventRecord(
    @Embedded
    val parent: XactEvent,
    @Relation(
        parentColumn = "id",
        entityColumn = "idEvent"
    )
    val children: List<XactRecord>
)
