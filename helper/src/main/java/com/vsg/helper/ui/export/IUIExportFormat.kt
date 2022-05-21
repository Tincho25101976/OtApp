package com.vsg.helper.ui.export

import com.vsg.helper.common.export.IEntityExport

interface IUIExportFormat<TEntity> : IUIExportToFile<TEntity>
        where TEntity : IEntityExport {
    fun toDataString(data: TEntity): String?
}