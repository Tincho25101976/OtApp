package com.vsg.helper.ui.widget.spinner.choose

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleListIdRelation
import com.vsg.helper.common.util.viewModel.IViewModelView
import com.vsg.helper.ui.adapter.IDataAdapterTitle
import com.vsg.helper.ui.util.BaseActivity

abstract class ChooseIEntitySpinnerWithParameter<TEntity, TViewModel, TParameter>(
    app: BaseActivity,
    @StringRes text: Int,
    type: Class<TViewModel>,
    private val parameter: TParameter
) : ChooseIEntitySpinner<TEntity, TViewModel>(app, text, type)
        where TEntity : IEntity,
              TEntity : IDataAdapterTitle,
              TViewModel : ViewModel,
              TViewModel : IViewModelAllSimpleListIdRelation<TEntity>,
              TViewModel : IViewModelView<TEntity>,
              TParameter : IEntity {

    override fun getDataSource(type: Class<TViewModel>): List<TEntity>? =
        makeViewModel(type).viewModelViewAllSimpleList(parameter.id)
}