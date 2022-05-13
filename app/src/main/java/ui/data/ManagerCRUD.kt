package com.vsg.ot.ui.data

import android.content.Context
import com.vsg.helper.common.adapter.IDataAdapter
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.*
import com.vsg.helper.helper.Helper.Companion.or
import com.vsg.helper.helper.Helper.Companion.then
import com.vsg.helper.helper.progress.ICallbackProcessGetAdapter
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.ui.adapter.UIIDataAdapter
import com.vsg.helper.ui.data.ILog
import com.vsg.helper.ui.data.IReadToList
import com.vsg.helper.ui.data.log.CustomLog
import com.vsg.helper.ui.data.source.helper.XmlParseHelper
import com.vsg.helper.util.helper.HelperNumeric.Companion.toPadStart
import com.vsg.ot.common.model.init.dao.DaoGenericOtParse
import java.io.File
import java.io.FileInputStream

class ManagerCRUD<T>(
    private var contex: Context,
    dbName: String,
    item: T,
    private var fileXML: String,
    private val db: DaoGenericOtParse<T>
) :
    ICallbackProcessGetAdapter<T>
        where T : IEntityParse<T>,
              T : IDataAdapter,
              T : IResultRecyclerAdapter,
              T : IEntityPagingLayoutPosition,
              T : IEntity,
              T : IIsEnabled,
              T : IEntityCreateDate,
              T : Comparable<T> {
    private val xml: XmlParseHelper<T>

    override var onProgress: ((Int, Int, Double) -> Unit)? = null

    init {
        try {
            if (dbName.isEmpty()) throw Exception("El Nombre de la base de datos no puede ser nula...")
            xml = XmlParseHelper(item)
            db.onProgress = { actual, total, porcentaje ->
                Thread { onResultProgreso(actual, total, porcentaje) }.start()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private lateinit var read: MutableList<T>
    private var sb = StringBuilder()
    var type: TypeManagerCRUD = TypeManagerCRUD.DEFAULT

    private fun readFileXML() {
        if (fileXML.isEmpty()) sb.append("No se ha especificado el nombre del archivo")
        val file = File(fileXML)
        if (file.exists()) {
            FileInputStream(file).use {
                xml.getParse(it)
            }
        }
    }

    fun getReadXML(): UIIDataAdapter<T> {
        clear()
        if (fileXML.isEmpty()) {
            sb.toLineSpanned("Observación,", "No se ha especificado el nombre del archivo")
            return getAdapter()
        }
        readFileXML()
        val adapter = set(xml)
        type = TypeManagerCRUD.READ_XML
        return adapter
    }

    fun getSelect(): UIIDataAdapter<T> {
        clear()
        val adapter = set(db)
        type = TypeManagerCRUD.SELECT_DB
        return adapter
    }

    fun getUpdate(): UIIDataAdapter<T> {
        clear()
        type = TypeManagerCRUD.UPDATE_DB
        try {
            if (fileXML.isEmpty()) {
                sb.append("No se ha especificado el nombre del archivo").appendLine()
                return getAdapter()
            }
            readFileXML()
            val dataXML = xml.readToList()
            sb.append(xml.getLog()).appendLine()

            db.deleteAll()
            sb.append(db.getLog()).appendLine()

            db.processWithProgress(dataXML)
            sb.append(db.getLog()).appendLine()

            this.read = db.readToList()
            val count = read.count()
            sb.append(db.getLog()).appendLine()

            val result = dataXML.count() == count
            val tempSb = StringBuilder().append("Resumen:").appendLine()
            tempSb.append("La actualización de la fuente de datos").appendLine()
            tempSb.append(
                "${!result then "" or "no "}presento problemas"
            ).appendLine()
            tempSb.append(
                "Se han insertado: ${count.toPadStart()} ${(count > 1) then "registros" or "registro"}"
            ).appendLine()
            sb.insert(0, tempSb).appendLine().append(CustomLog.LINE).appendLine().appendLine()
        } catch (e: Exception) {
            sb.insert(0, e.message).appendLine().append(CustomLog.LINE).appendLine().appendLine()
        }
        return getAdapter()
    }

    fun getDelete(): UIIDataAdapter<T> {
        clear(false)
        db.deleteAll()
        val adapter = set(db)
        type = TypeManagerCRUD.DELETE_DB
        return adapter
    }

    fun getLog(): String = sb.toString()

    override fun processGetAdapter(): UIIDataAdapter<T> = getUpdate()

    private fun getAdapter(): UIIDataAdapter<T> = UIIDataAdapter(contex, read)
    private fun <R> set(data: R): UIIDataAdapter<T> where R : IReadToList<T>, R : ILog {
        clear()
        read = data.readToList()
        sb.append(data.getLog())
        return getAdapter()
    }

    private fun clear(clearLog: Boolean = true) {
        type = TypeManagerCRUD.DEFAULT
        if (clearLog) sb = StringBuilder()
        read = mutableListOf()
    }

    //region delegate
    private fun onResultProgreso(actual: Int, total: Int, porcentaje: Double) {
        onProgress?.invoke(actual, total, porcentaje)
    }
    //endregion

    companion object {
        enum class TypeManagerCRUD {
            READ_XML, SELECT_DB, UPDATE_DB, DELETE_DB, DEFAULT
        }
    }
}