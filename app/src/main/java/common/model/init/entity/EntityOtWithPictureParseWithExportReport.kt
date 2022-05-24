package com.vsg.ot.common.model.init.entity

import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.export.ExportGenericEntity
import com.vsg.helper.common.export.ExportGenericEntityItem
import com.vsg.helper.common.export.IEntityExport
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IReference
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.report.IEntityReport
import com.vsg.helper.common.util.addItem.IAddItemEntity

abstract class EntityOtWithPictureParseWithExportReport<T> :
    EntityOtWithPictureParseWithExport<T>(), IEntityReport
        where T : ItemBase,
              T : IResultRecyclerAdapter,
              T : IAddItemEntity,
              T : IReference,
              T : IEntityExport,
              T : IEntityReport,
              T : IEntityPagingLayoutPosition {

    abstract fun aGetReportItem(): List<ExportGenericEntityItem<*>>
    override fun report(): ExportGenericEntity {
        val result = ExportGenericEntity(this::class.java)
        result.items.addAll(aGetReportItem())
        return result
    }
}