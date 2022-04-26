package com.vsg.helper.ui.crud

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.view.children
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IIsDefault
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.common.popup.IPopUpData
import com.vsg.helper.common.popup.IResultPopUpData
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.viewModel.MakeGenericViewModel
import com.vsg.helper.helper.font.FontManager.Static.typeFaceCustom
import com.vsg.helper.ui.popup.dialog.UICustomAlertDialog
import com.vsg.helper.ui.popup.dialog.UICustomAlertDialogParameter
import com.vsg.helper.ui.popup.dialog.UICustomAlertDialogResult
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.spinner.CustomSpinner
import com.vsg.helper.ui.widget.text.CustomInputText
import kotlin.reflect.full.createInstance

abstract class UICustomCRUD<TActivity, TViewModel, TDao, TEntity>(
    val activity: TActivity,
    protected var operation: DBOperation,
    @LayoutRes customLayout: Int
) :
    UICustomAlertDialogParameter(customLayout)
        where TActivity : CurrentBaseActivity<TViewModel, TDao, TEntity>,
              TViewModel : MakeGenericViewModel<TDao, TEntity>,
              TDao : IGenericDao<TEntity>,
              TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : IResultPopUpData {

    //region properties
    private var db: TViewModel = activity.currentViewModel()
    var item: TEntity? = null
    private var dialog: UICustomAlertDialogResult<TActivity> = UICustomAlertDialogResult(activity)
    private var dialogView: UICustomAlertDialog<TActivity> = UICustomAlertDialog(activity)
    protected var container: View? = null
    protected var containerLoad: Boolean = false
    override var title: String = ""
        get() = operation.data
    //endregion

    //region handler
    var onEventCRUD: ((Boolean) -> Unit)? = null
    protected var onEventSetContainer: ((DBOperation) -> Unit)? = null
    protected var onEventCRUDOperation: ((TViewModel, DBOperation, TEntity) -> Boolean)? = null
    protected var onEventGetView: ((View, TViewModel) -> Unit)? = null
    protected var onSetOKEnabled: ((DBOperation) -> Boolean)? = null
    protected var onEventGetPopUpDataParameter: ((IPopUpData?, TEntity?) -> IPopUpData?)? = null
    //endregion

    //region init
    init {
        this.factorHeight = 0.6
        dialog.onEventSetViewContainer = {
            this.container = it
            if (container != null) onEventGetView?.invoke(it, db)

            when (operation) {
                DBOperation.INSERT -> onEventSetContainer?.invoke(operation)
                DBOperation.UPDATE -> {
                    setOperation()
                    onEventSetContainer?.invoke(operation)
                }
                else -> Unit
            }
            if(it is ViewGroup) findAndSetTypeface(it, activity.typeFaceCustom(Typeface.BOLD_ITALIC))
        }
        dialog.onEventClickOK = { if (crud()) dialog }
        dialog.onEventSetOKEnabled = { onSetOKEnabled?.invoke(operation) ?: true }
        dialogView.onEventClickOK = { if (crud()) dialog }
    }
    //endregion

    //region container
    protected abstract fun aActionForSetOperation()
    private fun setOperation() {
        if (item == null) throw Exception("En la operación de ${operation.data} el objeto ítem no puede ser nulo...")
        aActionForSetOperation()
    }
    //endregion

    //region functional
    protected fun isOperationView(): Boolean =
        (operation == DBOperation.UPDATE || operation == DBOperation.DELETE || operation == DBOperation.VIEW)

    protected fun isOperationDeleteOrView(): Boolean =
        (operation == DBOperation.DELETE || operation == DBOperation.VIEW)

    protected fun isOperationEdit(): Boolean = (operation == DBOperation.UPDATE)
    protected fun isOperationNewOrEdit(): Boolean =
        (operation == DBOperation.UPDATE || operation == DBOperation.INSERT)
    protected fun getStringText(@StringRes value: Int): String = activity.getString(value)
    //endregion

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

    //region item
    protected abstract fun aGetEntityAllowDefault(): Boolean
    protected abstract fun aGetItem(): TEntity?
    //endregion

    //region make
    fun make() = when (isOperationDeleteOrView()) {
        true -> {
            var popup: IPopUpData? = item?.getPopUp()
            popup?.commandOK = operation == DBOperation.DELETE
            popup = onEventGetPopUpDataParameter?.invoke(popup, item) ?: popup
            dialogView.make(popup)
        }
        false -> dialog.make(this)
    }
    //endregion

    // region crud
    private fun crud(): Boolean {
        if (operation == DBOperation.INDEFINIDO) {
            return false
        }
        val item: TEntity = aGetItem() ?: throw Exception("Error al crear el ítem...")
        val result: Boolean = onEventCRUDOperation?.invoke(db, operation, item) ?: false
        onEventCRUD?.invoke(result)
        return result
    }
    //endregion

    //region message
    fun message(data: String) {
        if (data.isEmpty()) return
        Toast.makeText(activity, data, Toast.LENGTH_LONG).show()
    }
    //endregion

    protected inline fun <reified R : IIsDefault> isEntityAllowDefault(): Boolean {
        return R::class.createInstance().allowDefaultValue
    }
}