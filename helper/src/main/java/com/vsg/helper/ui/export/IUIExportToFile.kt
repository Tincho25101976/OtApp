package com.vsg.helper.ui.export

import com.vsg.helper.common.export.IEntityExport
import com.vsg.helper.common.model.IEntity

interface IUIExportToFile<TEntity> : IUIEntityToFile<TEntity>
        where TEntity : IEntityExport,
              TEntity : IEntity {
//    override fun toFile(data: TEntity, activity: Activity, path: String): File?
}