package com.vsg.helper.ui.data.ui

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.children
import com.vsg.helper.common.adapter.IDataAdapter
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.*
import com.vsg.helper.common.model.viewModel.ViewModelGenericParse
import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.common.util.dao.IGenericDaoPagingParse
import com.vsg.helper.helper.Helper.Companion.or
import com.vsg.helper.helper.Helper.Companion.then
import com.vsg.helper.helper.font.FontManager.Static.typeFaceCustom
import com.vsg.helper.helper.progress.ICallbackProcessGetAdapter
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.ui.adapter.UIIDataAdapter
import com.vsg.helper.ui.data.ILog
import com.vsg.helper.ui.data.IReadToList
import com.vsg.helper.ui.data.log.CustomLog
import com.vsg.helper.ui.data.source.helper.XmlParseHelper
import com.vsg.helper.ui.popup.dialog.UICustomAlertDialogParameter
import com.vsg.helper.ui.popup.dialog.UICustomAlertDialogResult
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.spinner.CustomSpinner
import com.vsg.helper.ui.widget.text.CustomInputText
import com.vsg.helper.util.helper.HelperNumeric.Companion.toPadStart
import java.io.File
import java.io.FileInputStream

@ExperimentalStdlibApi
abstract class ManagerCRUD<TActivity, TViewModel, TEntity, TDao>(
    val activity: TActivity,
    val item: TEntity,
    private var fileXML: String,
    @LayoutRes customLayout: Int
) :
    UICustomAlertDialogParameter(customLayout),
    ICallbackProcessGetAdapter<TEntity>
        where TActivity : CurrentBaseActivity<TViewModel, TDao, TEntity>,
              TViewModel : ViewModelGenericParse<TDao, TEntity>,
              TEntity : IEntityParse<TEntity>,
              TEntity : IDataAdapter,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : IEntityCreateDate,
              TEntity : Comparable<TEntity>,
              TDao : IGenericDaoPagingParse<TEntity> {

    //region events
    var onEventCRUD: ((Boolean) -> Unit)? = null
    override var onProgress: ((Int, Int, Double) -> Unit)? = null
    protected var onEventGetView: ((View, TViewModel) -> Unit)? = null
    protected var onEventSetContainer: ((DBOperation) -> Unit)? = null
    protected var onSetOKEnabled: ((DBOperation) -> Boolean)? = null
    protected var onEventCRUDOperation: ((TViewModel, TEntity) -> Boolean)? = null
    //endregion

    //region properties
    private var db: TViewModel = activity.currentViewModel()
    private val xml: XmlParseHelper<TEntity>
    private lateinit var read: MutableList<TEntity>
    private var sb = StringBuilder()
    var type: TypeManagerCRUD = TypeManagerCRUD.DEFAULT
    private var dialog: UICustomAlertDialogResult<TActivity> = UICustomAlertDialogResult(activity)
    protected var container: View? = null
    protected var containerLoad: Boolean = false
    //endregion

    init {
        try {
            xml = XmlParseHelper(item)
            db.onProgress = { actual, total, porcentaje ->
                Thread { onResultProgreso(actual, total, porcentaje) }.start()
            }
            dialog.onEventSetViewContainer = {
                this.container = it
                if (container != null) onEventGetView?.invoke(it, db)

//                when (operation) {
//                    DBOperation.INSERT -> onEventSetContainer?.invoke(operation)
//                    DBOperation.UPDATE -> {
//                        setOperation()
//                        onEventSetContainer?.invoke(operation)
//                    }
//                    else -> Unit
//                }
                if (it is ViewGroup) findAndSetTypeface(
                    it,
                    activity.typeFaceCustom(Typeface.BOLD_ITALIC)
                )
            }
            dialog.onEventClickOK = { if (crud()) dialog }
//            dialog.onEventSetOKEnabled = { onSetOKEnabled?.invoke(operation) ?: true }
//            dialogView.onEventClickOK = { if (crud()) dialog }
        } catch (e: Exception) {
            throw e
        }
    }

    //region methods
    protected abstract fun aGetItem(): TEntity?

    //region typeface
    protected fun findAndSetTypeface(view: ViewGroup, typeface: Typeface) {
        view.children.forEach {
            if (it is ViewGroup) findAndSetTypeface(it, typeface)
            if (it is TextView) it.typeface = typeface
            if (it is EditText) it.typeface = typeface
            if (it is CustomInputText) it.typeface = typeface
            if (it is CustomSpinner) it.setTypeface(typeface)
        }
    }
    //endregion

    private fun readFileXML() {
        if (fileXML.isEmpty()) sb.append("No se ha especificado el nombre del archivo")
        val file = File(fileXML)
        if (file.exists()) {
            FileInputStream(file).use {
                xml.getParse(it)
            }
        }
    }

    fun getReadXML(): UIIDataAdapter<TEntity> {
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

    //region make
    fun make() {
        dialog.make(this)
    }
    //endregion

    // region crud
    private fun crud(): Boolean {
//        if (operation == DBOperation.INDEFINIDO) {
//            return false
//        }
        val item: TEntity = aGetItem() ?: throw Exception("Error al crear el ítem...")
        val result: Boolean = onEventCRUDOperation?.invoke(db, item) ?: false
        onEventCRUD?.invoke(result)
        return result
    }
    //endregion
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