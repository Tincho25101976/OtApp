package com.vsg.helper.ui.crud

import android.graphics.Color
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.viewModel.MakeGenericViewModel
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.text.CustomInputText

abstract class UICustomCRUDViewModelRelation<TActivity, TViewModel, TDao, TEntity, TParent>
    (
    activity: TActivity,
    operation: DBOperation,
    @LayoutRes layout: Int,
    viewModel: TViewModel? = null,
    val parent: TParent,
    @IdRes val idTextParent: Int
) : UICustomCRUDViewModel<TActivity, TViewModel, TDao, TEntity>(
    activity,
    operation,
    layout,
    viewModel
) where TActivity : CurrentBaseActivity<TViewModel, TDao, TEntity>,
        TViewModel : MakeGenericViewModel<TDao, TEntity>,
        TDao : IGenericDao<TEntity>,
        TEntity : ItemBase,
        TParent : ItemBase {

    //widget
    private lateinit var tParent: CustomInputText
    //endregion

    //region definition
    protected abstract fun aGetTextParent(): String
    //endregion

    override fun oFinishInit(view: View) {
        this.tParent = view.findViewById<CustomInputText>(idTextParent).apply {
            this.setReadOnlyText(
                aGetTextParent(),
                backgroundColor = Color.TRANSPARENT,
                rules = arrayOf(RelativeLayout.ALIGN_PARENT_TOP)
            )
        }
    }


}