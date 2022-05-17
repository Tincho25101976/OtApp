package com.vsg.helper.ui.data.ui

import com.vsg.helper.common.adapter.IDataAdapter
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.*
import com.vsg.helper.common.model.viewModel.ViewModelGenericParse
import com.vsg.helper.common.util.dao.IGenericDaoPagingParse
import com.vsg.helper.helper.Helper.Companion.or
import com.vsg.helper.helper.Helper.Companion.then
import com.vsg.helper.helper.progress.ICallbackProcessGetAdapter
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.ui.adapter.UIIDataAdapter
import com.vsg.helper.ui.data.ILog
import com.vsg.helper.ui.data.IReadToList
import com.vsg.helper.ui.data.log.CustomLog
import com.vsg.helper.ui.data.source.helper.XmlParseHelper
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.util.helper.HelperNumeric.Companion.toPadStart
import java.io.File
import java.io.FileInputStream

@ExperimentalStdlibApi
class ManagerCRUD<TActivity, TViewModel, TDao, TEntity>(
    val activity: TActivity,
    val item: TEntity,
    private val fileUpload: String
) : ICallbackProcessGetAdapter<TEntity>
        where TActivity : CurrentBaseActivity<TViewModel, TDao, TEntity>,
              TViewModel : ViewModelGenericParse<TDao, TEntity>,
              TEntity : IEntityParse<TEntity>,
              TEntity : IDataAdapter,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : IEntityCode,
              TEntity : ItemBase,
              TEntity : IEntityCreateDate,
              TEntity : Comparable<TEntity>,
              TDao : IGenericDaoPagingParse<TEntity> {

    //region events
    override var onProgress: ((Int, Int, Double) -> Unit)? = null
    //endregion

    //region properties
    private var db: TViewModel = activity.currentViewModel()
    private val xml: XmlParseHelper<TEntity>
    private lateinit var read: MutableList<TEntity>
    private var sb = StringBuilder()
    var type: TypeManagerCRUD = TypeManagerCRUD.DEFAULT
    //endregion

    init {
        try {
            xml = XmlParseHelper(this.item)
            db.onProgress = { actual, total, porcentaje ->
                Thread { onResultProgreso(actual, total, porcentaje) }.start()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    //region methods


    private fun readFileXML() {
        if (fileUpload.isEmpty()) sb.append("No se ha especificado el nombre del archivo")
        val file = File(fileUpload)
        if (file.exists()) {
            FileInputStream(file).use {
                xml.getParse(it)
            }
        }
    }

    fun getReadXML(): UIIDataAdapter<TEntity> {
        clear()
        if (fileUpload.isEmpty()) {
            sb.toLineSpanned("Observación,", "No se ha especificado el nombre del archivo")
            return getAdapter()
        }
        readFileXML()
        val adapter = set(xml)
        type = TypeManagerCRUD.READ_XML
        return adapter
    }

    fun getSelect(): UIIDataAdapter<TEntity> {
        clear()
        val adapter = set(db)
        type = TypeManagerCRUD.SELECT_DB
        return adapter
    }

    fun getUpdate(): UIIDataAdapter<TEntity> {
        clear()
        type = TypeManagerCRUD.UPDATE_DB
        try {
            if (fileUpload.isEmpty()) {
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

    fun getDelete(): UIIDataAdapter<TEntity> {
        clear(false)
        db.deleteAll()
        val adapter = set(db)
        type = TypeManagerCRUD.DELETE_DB
        return adapter
    }

    fun getLog(): String = sb.toString()

    override fun processGetAdapter(): UIIDataAdapter<TEntity> = getUpdate()

    private fun getAdapter(): UIIDataAdapter<TEntity> = UIIDataAdapter(activity, read)
    private fun <R> set(data: R): UIIDataAdapter<TEntity> where R : IReadToList<TEntity>, R : ILog {
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
//endregion

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