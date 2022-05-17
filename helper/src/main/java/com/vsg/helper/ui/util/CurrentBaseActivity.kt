package com.vsg.helper.ui.util

import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.viewModel.IViewModelView
import com.vsg.helper.common.util.viewModel.MakeGenericViewModel

abstract class CurrentBaseActivity<TViewModel, TDao, TEntity>(
    @LayoutRes view: Int,
    private val type: Class<TViewModel>
) : BaseActivity(view)
        where TViewModel : MakeGenericViewModel<TDao, TEntity>,
              TDao : IGenericDao<TEntity>,
              TEntity : IEntity,
              TEntity : IIsEnabled {
    private lateinit var viewModel: TViewModel
    private lateinit var context: Any

    protected abstract fun aSetContext(): Any

    init {
        setContext()
        this.onEventExecuteSetViewModel = {
            viewModel = run {
                ViewModelProvider(it)[type]
            }
        }
        this.onEventLoadBackground = { v, background ->
            v.setBackgroundResource(background)
        }
    }

    private fun setContext() {
        this.context = aSetContext()
    }

    //region viewModel
    fun <TParent, TViewModelParent> makeViewModel(type: Class<TViewModelParent>): TViewModelParent
            where TViewModelParent : ViewModel,
                  TViewModelParent : IViewModelView<TParent>,
                  TParent : IEntity {
        return run { ViewModelProvider(this).get(type) }
    }
    //endregion


    fun currentViewModel(): TViewModel = viewModel
    protected fun <TContext> context(): TContext where TContext : BaseActivity, TContext : LifecycleOwner =
        context as TContext
}