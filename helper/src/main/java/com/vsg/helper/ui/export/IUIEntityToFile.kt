package com.vsg.helper.ui.export

import android.app.Activity
import com.vsg.helper.common.model.IEntity
import java.io.File

interface IUIEntityToFile<TEntity>
        where TEntity : IEntity {
    fun toFile(data: TEntity, activity: Activity, path: String): File?
}