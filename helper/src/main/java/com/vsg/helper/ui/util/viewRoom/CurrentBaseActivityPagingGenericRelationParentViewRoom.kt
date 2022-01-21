package com.vsg.helper.ui.util.viewRoom

import androidx.paging.PagingData
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityHasItem
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.model.viewRoom.IEntityViewRoom
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.dao.viewRoom.IGenericDaoPagingRelationViewRoom
import com.vsg.helper.common.util.viewModel.*
import com.vsg.helper.common.util.viewModel.viewRoom.MakeGenericViewModelViewRoom
import com.vsg.helper.ui.adapter.IDataAdapterEnum
import com.vsg.helper.ui.adapter.IDataAdapterTitle
import com.vsg.helper.ui.popup.select.UIAlertDialogResultEntity
import com.vsg.helper.ui.util.TypeOriginParent

abstract class CurrentBaseActivityPagingGenericRelationParentViewRoom<TActivity, TViewModel, TDao, TEntity, TViewRoom, TFilter,
        TFilterParent, TParentViewModel, TParentDao, TParent, TParentFilterHasItems>
    (
    type: Class<TViewModel>,
    typeParent: Class<TParentViewModel>,
    typeClassFilter: Class<TFilter>,
    typeClassFilterParent: Class<TFilterParent>
) : CurrentBaseActivityPagingGenericRelationParentBaseViewRoom<TActivity, TViewModel, TDao, TEntity, TViewRoom, TFilter,
        TFilterParent, TParentViewModel, TParentDao, TParent, TParentFilterHasItems>(
    type,
    typeParent,
    typeClassFilter,
    typeClassFilterParent
)
        where TActivity : CurrentBaseActivityViewRoom<TViewModel, TDao, TEntity, TViewRoom>,
              TViewModel : MakeGenericViewModelViewRoom<TDao, TEntity, TViewRoom>,
              TDao : IGenericDaoPagingRelationViewRoom<TEntity, TViewRoom>,
              TEntity : IEntityViewRoom<TEntity>,
              TFilter : IDataAdapterEnum,
              TFilter : Enum<TFilter>,
              TViewRoom : IEntity,

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