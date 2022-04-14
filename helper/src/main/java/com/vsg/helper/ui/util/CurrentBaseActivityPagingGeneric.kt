package com.vsg.helper.ui.util

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.viewModel.IViewModelAllPaging
import com.vsg.helper.common.util.viewModel.IViewModelAllTextSearch
import com.vsg.helper.common.util.viewModel.MakeGenericViewModel
import com.vsg.helper.ui.adapter.IDataAdapterEnum
import com.vsg.helper.ui.crud.UICustomCRUDViewModel
import kotlinx.coroutines.flow.Flow

abstract class CurrentBaseActivityPagingGeneric<TActivity, TViewModel, TDao, TEntity, TFilter, TCrud>(
    type: Class<TViewModel>,
    typeFilter: Class<TFilter>
) :
    CurrentBaseActivityPagingBase<TActivity, TViewModel, TDao, TEntity, TFilter, TCrud>(
        type,
        typeFilter
    )
        where TActivity : CurrentBaseActivity<TViewModel, TDao, TEntity>,
              TViewModel : MakeGenericViewModel<TDao, TEntity>,
              TViewModel : IViewModelAllPaging<TEntity>,
              TViewModel : IViewModelAllTextSearch,
              TDao : IGenericDao<TEntity>,
              TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity>,
              TFilter : IDataAdapterEnum,
              TFilter : Enum<TFilter>,
              TEntity : ItemBase,
              TCrud : UICustomCRUDViewModel<TActivity, TViewModel, TDao, TEntity> {

    override fun oGetViewModelGetAllTextSearch(): LiveData<List<String>> =
        currentViewModel().viewModelGetAllTextSearch()

    override fun oGetViewModelGetViewAllPaging(): Flow<PagingData<TEntity>> =
        currentViewModel().viewModelGetViewAllPaging()
}