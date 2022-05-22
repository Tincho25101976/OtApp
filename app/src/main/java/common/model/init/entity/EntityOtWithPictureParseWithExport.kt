package com.vsg.ot.common.model.init.entity

import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.export.ExportGenericEntity
import com.vsg.helper.common.export.ExportGenericEntityItem
import com.vsg.helper.common.export.ExportType
import com.vsg.helper.common.export.IEntityExport
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IReference
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.util.addItem.IAddItemEntity

abstract class EntityOtWithPictureParseWithExport<T> :
    EntityOtWithPictureParse<T>(), IEntityExport
        where T : ItemBase,
              T : IResultRecyclerAdapter,
              T : IAddItemEntity,
              T : IReference,
              T : IEntityExport,
              T : IEntityPagingLayoutPosition {

    override fun nameFile(exportType: ExportType): String = "${tag}Source.${exportType.extension}"

    abstract fun aGetExportItem(): List<ExportGenericEntityItem<*>>
    override fun export(): ExportGenericEntity {
        val result = ExportGenericEntity(this::class.java)
        result.items.addAll(aGetExportItem())
        return result
    }
}