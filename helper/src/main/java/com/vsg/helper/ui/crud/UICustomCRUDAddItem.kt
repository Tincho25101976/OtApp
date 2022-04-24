package com.vsg.helper.ui.crud

import android.view.View
import android.widget.CheckBox
import androidx.annotation.LayoutRes
import com.vsg.helper.R
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.viewModel.MakeGenericViewModel
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.text.CustomInputText

abstract class UICustomCRUDAddItem<TActivity, TViewModel, TDao, TEntity, TEntityParent>(
    activity: TActivity,
    operation: DBOperation,
    @LayoutRes layout: Int,
    protected val parent: TEntityParent,
    protected val viewModel: TViewModel? = null
) :
    UICustomCRUD<TActivity, TViewModel, TDao, TEntity>(
        activity,
        operation,
        layout
    )
        where TActivity : CurrentBaseActivity<TViewModel, TDao, TEntity>,
              TViewModel : MakeGenericViewModel<TDao, TEntity>,
              TDao : IGenericDao<TEntity>,
              TEntity : ItemBase,
              TEntityParent : IEntity {

    //region handler
    var onEventSetInit: ((View) -> Unit)? = null
    var onEventGetNewEntity: (() -> TEntity?)? = null
    var onEventSetItem: ((TEntity) -> Unit)? = null
    var onEventSetItemsForClean: (() -> List<View>)? = null
    var onEventValidate: ((TEntity, DBOperation) -> Boolean?)? = null
    var onEventSetParametersForInsert: (() -> Unit)? = null
    //endregion

    //widget
    private lateinit var tDefault: CheckBox
    private lateinit var tEnabled: CheckBox
    private lateinit var tDescription: CustomInputText
    //endregion

    init {
        this.onEventGetView = { it, _ ->
            tDescription = it.findViewById(R.id.DialogGenericDescription)
            tEnabled = it.findViewById(R.id.DialogGenericIsEnabled)
            tDefault = it.findViewById<CheckBox>(R.id.DialogGenericIsDefault).apply {
                isEnabled = false
            }
            onEventSetInit?.invoke(it)
            containerLoad = container != null
        }
        this.onEventSetContainer = {
            setItem(item, it)
        }
        this.onEventCRUDOperation = { db, o, item ->
            var result = false
            var validate: Boolean? = isOperationDeleteOrView()
            if (isOperationNewOrEdit()) validate = onEventValidate?.invoke(item, o) ?: true
            if (validate != null && validate) {
                result = when (o) {
                    DBOperation.INSERT -> db.viewModelInsert(item)
                    DBOperation.UPDATE -> db.viewModelUpdate(item)
                    DBOperation.DELETE -> db.viewModelDelete(item)
                    else -> false
                }
            }
            result
        }
    }

    override fun aActionForSetOperation() {}
    private fun clean(o: DBOperation) {
        val itemsClean = onEventSetItemsForClean?.invoke()
        val items: MutableList<View> = mutableListOf()
        if (itemsClean != null && itemsClean.any()) items.addAll(itemsClean)
        items.addAll(arrayListOf(tDescription, tEnabled, tDefault))
        val data = activity.clearWidget(*items.toTypedArray())
        if (isOperationNewOrEdit()) {
            data?.forEach { it.isEnabled = true }
        }
        if (isOperationDeleteOrView()) {
            data?.forEach { it.isEnabled = false }
        }
        if (o == DBOperation.INSERT) {
            tEnabled.isChecked = true
            tEnabled.isEnabled = false
        }
        tDefault.apply {
            isEnabled = false
            isChecked = false
        }
    }

    private fun setItem(item: TEntity?, o: DBOperation) {
        clean(o)
        if (o == DBOperation.INSERT) onEventSetParametersForInsert?.invoke()
        if (!isOperationView()) return
        item ?: return
        onEventSetItem?.invoke(item)
        tDescription.text = item.description
        tEnabled.isChecked = item.isEnabled
        //tDefault.isChecked = item.isDefault
    }

    private fun makeItem(): TEntity? {
        val t: TEntity = onEventGetNewEntity?.invoke() ?: return null
        t.description = tDescription.text
        t.isDefault = tDefault.isChecked
        t.isEnabled = tEnabled.isChecked
        return t
    }

    override fun aGetItem(): TEntity? = when (operation) {
        DBOperation.INSERT -> makeItem()
        DBOperation.UPDATE -> when (item == null) {
            true -> null
            false -> makeItem()?.apply { id = item?.id!! }
        }
        DBOperation.DELETE -> item
        DBOperation.VIEW -> item
        else -> null
    }
}