package com.vsg.helper.ui.widget.spinner.choose

import android.widget.RelativeLayout
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.util.viewModel.IViewModelView
import com.vsg.helper.helper.HelperUI
import com.vsg.helper.helper.HelperUI.Static.setEditCustomLayoutRelativeLayout
import com.vsg.helper.helper.type.TypeMakeLayoutParameter
import com.vsg.helper.ui.adapter.IDataAdapterTitle
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.helper.ui.widget.spinner.CustomSpinner

abstract class ChooseIEntitySpinner<TEntity, TViewModel>(
    protected val app: BaseActivity,
    @StringRes text: Int,
    private val type: Class<TViewModel>
)
        where TEntity : IEntity,
              TEntity : IDataAdapterTitle,
              TViewModel : ViewModel,
              TViewModel : IViewModelView<TEntity> {
    //region handler
    var onEventItemSelected: ((TEntity?) -> Unit)? = null
//    var onEventGetDataSource: ((Class<TViewModel>) -> List<TEntity>?)? = null
//    var onEventAfterInit: (() -> Unit)? = null
    //endregion

    //region widget
    private var tSpinner: CustomSpinner
    private var tView: RelativeLayout? = null
    //endregion

    //region properties
    var item: TEntity? = null
        private set
    val view: RelativeLayout? get() = tView
    val spinner: CustomSpinner get() = tSpinner
    //endregion

    //region init
    init {
        tView = RelativeLayout(app).apply {
            layoutParams =
                HelperUI.makeCustomLayoutRelativeLayout(TypeMakeLayoutParameter.WIDTH_MATCH_HEIGHT_WRAP)
        }
        tSpinner = CustomSpinner(app, addId = true).apply {
            layoutParams = this.setEditCustomLayoutRelativeLayout().apply {
                addRule(RelativeLayout.ALIGN_PARENT_TOP)
            }
//            val data: List<TEntity>? = makeViewModel(type).viewModelViewAllSimpleList()
//            val tempData =
            val result: List<TEntity>? = getDataSource(type)
            if (result != null && result.any()) {
                this.setCustomAdapter(
                    result,
                    callBackItemSelect = {
                        this@ChooseIEntitySpinner.item = it
                        onEventItemSelected?.invoke(it)
                    }
                )
            }
            customTitle = app.getString(text)
//            customTextSize = resources.getInteger(R.integer.CustomSpinnerTitleTextSize).toFloat()
        }
        tView!!.addView(tSpinner)
//        onEventAfterInit?.invoke()
    }
    //endregion

    //region function

    //region viewModel
    protected fun <TViewModelParent> makeViewModel(type: Class<TViewModelParent>): TViewModelParent
            where TViewModelParent : ViewModel,
                  TViewModelParent : IViewModelView<TEntity> {
        return run {
            ViewModelProvider(app)[type]
        }
    }
    //endregion

    protected abstract fun getDataSource(type: Class<TViewModel>) :List<TEntity>?

    fun setItem(item: TEntity?) {
        if (item == null) return
        tSpinner.setItem<TEntity>(item.id)
    }

    fun setItem(item: Int) {
        if (item <= 0) return
        val data = makeViewModel(type).viewModelView(item) ?: return
        setItem(data)
    }
    //endregion
}