package com.vsg.helper.ui.util.viewRoom

import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.viewRoom.IEntityViewRoom
import com.vsg.helper.common.util.dao.viewRoom.IGenericDaoPagingRelationViewRoom
import com.vsg.helper.common.util.viewModel.IViewModelView
import com.vsg.helper.common.util.viewModel.viewRoom.MakeGenericViewModelViewRoom
import com.vsg.helper.ui.util.BaseActivity

abstract class CurrentBaseActivityViewRoom<TViewModel, TDao, TEntity, TViewRoom>(
    @LayoutRes view: Int,
    private val type: Class<TViewModel>
) : BaseActivity(view)
        where TViewModel : MakeGenericViewModelViewRoom<TDao, TEntity, TViewRoom>,
              TDao : IGenericDaoPagingRelationViewRoom<TEntity, TViewRoom>,
              TEntity : IEntityViewRoom<TEntity>,
              TViewRoom : IEntity {
    private lateinit var viewModel: TViewModel
    private lateinit var context: Any

    protected abstract fun aSetContext(): Any

    init {
        setContext()
        this.onEventExecuteSetViewModel = {
            viewModel = run {
                ViewModelProvider(it).get(type)
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
        (context as TContext)
}