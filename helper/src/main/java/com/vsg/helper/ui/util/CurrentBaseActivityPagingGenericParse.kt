package com.vsg.helper.ui.util

import com.vsg.helper.common.adapter.IDataAdapter
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.*
import com.vsg.helper.common.model.viewModel.ViewModelGenericParse
import com.vsg.helper.common.util.dao.IDaoAllTextSearch
import com.vsg.helper.common.util.dao.IGenericDaoPagingParse
import com.vsg.helper.common.util.viewModel.IViewModelAllPaging
import com.vsg.helper.common.util.viewModel.IViewModelAllTextSearch
import com.vsg.helper.ui.adapter.IDataAdapterEnum
import com.vsg.helper.ui.crud.UICustomCRUDViewModel

@ExperimentalStdlibApi
abstract class CurrentBaseActivityPagingGenericParse<TActivity, TViewModel, TDao, TEntity, TFilter, TCrud>(
    type: Class<TViewModel>,
    typeFilter: Class<TFilter>
) :
    CurrentBaseActivityPagingGeneric<TActivity, TViewModel, TDao, TEntity, TFilter, TCrud>(
        type,
        typeFilter
    )
        where TActivity : CurrentBaseActivity<TViewModel, TDao, TEntity>,
              TViewModel : IViewModelAllPaging<TEntity>,
              TViewModel : IViewModelAllTextSearch,
              TViewModel : ViewModelGenericParse<TDao, TEntity>,
              TDao : IGenericDaoPagingParse<TEntity>,
              TDao : IDaoAllTextSearch,
              TEntity : IEntity,
              TEntity : IEntityParse<TEntity>,
              TEntity : IDataAdapter,
              TEntity : IIsEnabled,
              TEntity : IEntityCreateDate,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity>,
              TFilter : IDataAdapterEnum,
              TFilter : Enum<TFilter>,
              TEntity : ItemBase,
              TCrud : UICustomCRUDViewModel<TActivity, TViewModel, TDao, TEntity>