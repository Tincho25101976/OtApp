package com.vsg.helper.ui.util

import androidx.paging.PagingData
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.*
import com.vsg.helper.common.util.dao.IDaoAllSimpleListRelation
import com.vsg.helper.common.util.dao.IDaoAllTextSearchRelation
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.dao.IGenericDaoPagingRelation
import com.vsg.helper.common.util.viewModel.*
import com.vsg.helper.ui.adapter.IDataAdapterEnum
import com.vsg.helper.ui.adapter.IDataAdapterTitle
import com.vsg.helper.ui.crud.UICustomCRUDViewModel
import com.vsg.helper.ui.popup.select.UIAlertDialogResultEntity

abstract class CurrentBaseActivityPagingGenericRelationParent<TActivity, TViewModel, TDao, TEntity, TFilter, TCrud,
        TFilterParent, TParentViewModel, TParentDao, TParent, TParentFilterHasItems>
    (
    type: Class<TViewModel>,
    typeParent: Class<TParentViewModel>,
    typeClassFilter: Class<TFilter>,
    typeClassFilterParent: Class<TFilterParent>
) : CurrentBaseActivityPagingGenericRelationParentBase<TActivity, TViewModel, TDao, TEntity, TFilter, TCrud,
        TFilterParent, TParentViewModel, TParentDao, TParent, TParentFilterHasItems>(
    type,
    typeParent,
    typeClassFilter,
    typeClassFilterParent
)
        where TActivity : CurrentBaseActivity<TViewModel, TDao, TEntity>,
              TViewModel : MakeGenericViewModelPagingRelation<TDao, TEntity>,
              TViewModel : IViewModelAllPagingRelation<TEntity>,
              TViewModel : IViewModelAllTextSearchRelation,
              TDao : IGenericDaoPagingRelation<TEntity>,
              TDao : IDaoAllTextSearchRelation,
              TDao : IDaoAllSimpleListRelation<TEntity>,
              TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity>,
              TFilter : IDataAdapterEnum,
              TFilter : Enum<TFilter>,
              TEntity : ItemBase,

              TCrud : UICustomCRUDViewModel<TActivity, TViewModel, TDao, TEntity>,

              TFilterParent : IDataAdapterEnum,
              TFilterParent : Enum<TFilterParent>,
              TParent : IEntity,
              TParent : IEntityHasItem,
              TParent : IIsEnabled,
              TParent : IResultRecyclerAdapter,
              TParent : IEntityPagingLayoutPosition,
              TParent : IDataAdapterTitle,
              TParent : Comparable<TParent>,
              TParentViewModel : MakeGenericViewModel<TParentDao, TParent>,
              TParentViewModel : IViewModelAllPaging<TParent>,
              TParentViewModel : IViewModelAllTextSearch,
              TParentViewModel : IViewModelHasItemsRelationType<TParentFilterHasItems>,
              TParentDao : IGenericDao<TParent>,
              TParentFilterHasItems : Enum<TParentFilterHasItems> {

    override fun aMakeCustomViewer() {
        MakeCustomViewer().apply {
            factorHeight = 0.45
            onEventClickOK = {
                setCurrentParent(it, TypeOriginParent.SELECTION, oHintForParent())
            }
            onEventGetItemAfterBind = { t ->
                val filter: TParentFilterHasItems? =
                    onEventSetParentFilterHasItems?.invoke()
                if (filter != null) t.hasItems =
                    this.currentViewModelResult.viewModelViewHasItems(t.id, filter)
                t
            }
            onEventMakeFilter = { item, find, it ->
                val filter: PagingData<TParent> =
                    onEventMakeFilterResult?.invoke(item, find, it) ?: it
                filter
            }
            make()
        }
    }

    inner class MakeCustomViewer :
        UIAlertDialogResultEntity<TActivity, TParentViewModel, TParent, TFilterParent>(
            aSetActivity(), makeViewModel(typeParent), typeClassFilterParent
        )
}