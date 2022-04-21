package com.vsg.helper.ui.crud

import android.graphics.Typeface
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.vsg.helper.R
import com.vsg.helper.common.model.IIsDefault
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.viewModel.MakeGenericViewModel
import com.vsg.helper.helper.font.FontManager.Static.typeFaceCustom
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.spinner.CustomSpinner
import com.vsg.helper.ui.widget.text.CustomInputText
import kotlin.reflect.full.createInstance

abstract class UICustomCRUDViewModel<TActivity, TViewModel, TDao, TEntity>(
    activity: TActivity,
    operation: DBOperation,
    @LayoutRes layout: Int,
    val viewModel: TViewModel? = null
) :
    UICustomCRUD<TActivity, TViewModel, TDao, TEntity>(
        activity,
        operation,
        layout
    )
        where TActivity : CurrentBaseActivity<TViewModel, TDao, TEntity>,
              TViewModel : MakeGenericViewModel<TDao, TEntity>,
              TDao : IGenericDao<TEntity>,
              TEntity : ItemBase {

    //region handler
    var onEventSetInit: ((View) -> Unit)? = null
    var onEventGetNewOrUpdateEntity: ((TEntity?) -> TEntity?)? = null
    var onEventSetItem: ((TEntity) -> Unit)? = null
    var onEventSetItemsForClean: (() -> List<View>)? = null
    var onEventValidate: ((TEntity, DBOperation) -> Boolean?)? = null
    var onEventSetParametersForInsert: (() -> Unit)? = null
    //endregion

    //region widget
    private lateinit var tDefault: CheckBox
    private lateinit var tEnabled: CheckBox
    private lateinit var tDescription: CustomInputText
    //endregion

    //region properties
    private val defaultTypeface: Typeface
        get() = activity.typeFaceCustom(Typeface.BOLD_ITALIC)
    protected val viewsFont: MutableList<View> = mutableListOf()
    //endregion

    init {
        this.onEventGetView = { it, _ ->
            tDescription = it.findViewById(R.id.DialogGenericDescription)
            tEnabled = it.findViewById(R.id.DialogGenericIsEnabled)
            tDefault = it.findViewById(R.id.DialogGenericIsDefault)

            viewsFont.addAll(arrayOf(tDescription, tEnabled, tDefault))

            onEventSetInit?.invoke(it)

            val valueSizeFont =
                activity.resources.getInteger(R.integer.CustomAdapterTextSize).toFloat()

            viewsFont.forEach {
                when(it){
                    is TextView -> it.textSize = valueSizeFont
                    is CustomInputText -> it.textSize = valueSizeFont
                    is CustomSpinner -> it.customTextSize = valueSizeFont
                }
            }

            containerLoad = container != null
            if (containerLoad) oFinishInit(it)


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

    protected open fun oFinishInit(view: View) {}
    override fun aActionForSetOperation() {}
    protected open fun oIsEntityOnlyOneDefault() = false
    private fun clean(o: DBOperation) {
        val itemsClean = onEventSetItemsForClean?.invoke()
        val items: MutableList<View> = mutableListOf()
        if (itemsClean != null && itemsClean.any()) items.addAll(itemsClean)
        items.addAll(arrayListOf(tDescription, tEnabled, tDefault))
        val data = activity.clearWidget(*items.toTypedArray())
        if (isOperationNewOrEdit()) data?.forEach { it.isEnabled = true }
        if (isOperationDeleteOrView()) data?.forEach { it.isEnabled = false }
        if (o == DBOperation.INSERT) {
            tEnabled.isChecked = true
            tEnabled.isEnabled = false
        }
        if (oIsEntityOnlyOneDefault()) {
            tDefault.apply {
                isEnabled = false
                isChecked = false
            }
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
        tDefault.isChecked = item.isDefault
    }

    private fun makeItem(item: TEntity? = null): TEntity? {
        val temp: TEntity = onEventGetNewOrUpdateEntity?.invoke(item) ?: return null
        temp.isEnabled = tEnabled.isChecked
        temp.isDefault = tDefault.isChecked
        temp.description = tDescription.text
        return temp
    }

    override fun aGetItem(): TEntity? = when (operation) {
        DBOperation.INSERT -> makeItem()
        DBOperation.UPDATE -> when (item == null) {
            true -> null
            false -> makeItem(item)?.apply { id = item?.id!! }
        }
        DBOperation.DELETE -> item
        DBOperation.VIEW -> item
        else -> null
    }

    protected fun <T> setTypeface(vararg value: T) where T : TextView {
        if (!value.any()) return
        value.forEach { it.typeface = defaultTypeface }
    }

    protected inline fun <reified R : IIsDefault> isEntityOnlyOneDefaultTrue(): Boolean {
        return R::class.createInstance().isEntityOnlyDefault
    }
}