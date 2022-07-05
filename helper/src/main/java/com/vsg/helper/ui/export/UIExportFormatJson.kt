package com.vsg.helper.ui.export

import android.app.Activity
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.vsg.helper.common.export.ExportType
import com.vsg.helper.common.export.IEntityExport
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.helper.permission.HelperPermission.Static.checkedPermissionStorage
import java.io.File
import java.io.FileOutputStream

class UIExportFormatJson<TEntity> :
    IUIExportFormat<TEntity>
        where TEntity : IEntityExport,
              TEntity : IEntity {
    override fun toFile(data: TEntity, activity: Activity, path: String): File? {
        if (!data.export().hasItems) return null
        if (path.isEmpty()) return null
        return try {
            if (!activity.checkedPermissionStorage()) return null
            val fileName = data.nameFile(ExportType.JSON, true)
            val ruta: File? = activity.getExternalFilesDir(path)
            ruta?.mkdirs()
            val file = File(ruta, fileName)
            try {
                FileOutputStream(file).use {
                    getMapper().writeValue(it, data.export().values)
                    it.flush()
                }
                file
            } catch (e: Exception) {
                throw e
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun toDataString(data: TEntity): String? {
        return try {
            val result = getMapper().writeValueAsString(data.export().values)
            result
        } catch (e: Exception) {
            ""
        }
    }

    private fun getMapper(): ObjectMapper {
        return ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true)
    }
}