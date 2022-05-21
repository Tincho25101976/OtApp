package com.vsg.helper.ui.export

import android.app.Activity
import com.vsg.helper.common.export.IEntityExport
import java.io.File

interface IUIExportToFile<TEntity>
        where TEntity : IEntityExport {
    fun toFile(data: TEntity, activity: Activity, path: String): File?
}