package com.vsg.helper.ui.export

import com.vsg.helper.common.export.IEntityExport
import com.vsg.helper.common.model.IEntity

interface IUIExportFormat<TEntity> : IUIExportToFile<TEntity>
        where TEntity : IEntityExport,
              TEntity : IEntity {
    fun toDataString(data: TEntity): String?
}