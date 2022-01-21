package com.vsg.helper.ui.util.viewRoom

import android.graphics.Color
import android.widget.RelativeLayout
import androidx.paging.PagingData
import com.vsg.helper.R
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.*
import com.vsg.helper.common.model.viewRoom.IEntityViewRoom
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.dao.viewRoom.IGenericDaoPagingRelationViewRoom
import com.vsg.helper.common.util.viewModel.*
import com.vsg.helper.common.util.viewModel.viewRoom.MakeGenericViewModelViewRoom
import com.vsg.helper.helper.HelperUI.Static.setEditCustomLayoutRelativeLayout
import com.vsg.helper.ui.adapter.IDataAdapterEnum
import com.vsg.helper.ui.adapter.IDataAdapterTitle
import com.vsg.helper.ui.util.TypeOriginParent
import com.vsg.helper.ui.widget.text.CustomInputText

abstract class CurrentBaseActivityPagingGenericRelationParentBaseViewRoom<TActivity, TViewModel, TDao, TEntity, TViewRoom, TFilter,
        TFilterParent, TParentViewModel, TParentDao, TParent, TParentFilterHasItems>
    (
    type: Class<TViewModel>,
    val typeParent: Class<TParentViewModel>,
    typeClassFilter: Class<TFilter>,
    val typeClassFilterParent: Class<TFilterParent>
) : CurrentBaseActivityPagingBaseViewRoom<TActivity, TViewModel, TDao, TEntity, TViewRoom, TFilter>(
    type,
    typeClassFilter
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
              TParent : Comparable<TParent>,
              TParent : IDataAdapterTitle,
              TParentViewModel : MakeGenericViewModel<TParentDao, TParent>,
              TParentViewModel : IViewModelHasItemsRelationType<TParentFilterHasItems>,
              TParentDao : IGenericDao<TParent>,
              TParentFilterHasItems : Enum<TParentFilterHasItems> {

    //region handler
    var onEventMakeFilterResult: ((TFilterParent, String, PagingData<TParent>) -> PagingData<TParent>)? =
        null
    var onEventSetParentFilterHasItems: (() -> TParentFilterHasItems)? = null
    //endregion

    //region parent
    private lateinit var tTextParent: CustomInputText
    var parent: TParent? = null
    private var typeOriginParent: TypeOriginParent = TypeOriginParent.UNDEFINED
    //endregion

    //region functional
    abstract fun aSetActivity(): TActivity
    protected abstract fun aCurrentListOfParent(): List<TParent>?

    init {
        onEventGetIdRelationFromIntent = {
            parent = getParent(typeParent)
            if (parent == null) {
                val tempParent = aCurrentListOfParent()
                if (tempParent != null) {
                    if (tempParent.count() > 1) aMakeCustomViewer()
                    if (tempParent.count() == 1) parent = tempParent.first()
                }
            }
        }
        onEventAfterSetContentView = {
            tTextParent = CustomInputText(this, addId = true).apply {
                this.setReadOnlyText(
                    text = "...",
                    size = 28,
                    backgroundColor = Color.LTGRAY,
                    rules = arrayOf(RelativeLayout.ALIGN_PARENT_TOP)
                )
                setBackgroundResource(R.drawable.shadow_view)
            }
            val tActivityPagingGenericSearchAndRecyclerView =
                it.findViewById<RelativeLayout>(R.id.ActivityPagingGenericSearchAndRecyclerView)
                    .apply {
                        this.setEditCustomLayoutRelativeLayout().apply {
                            addRule(RelativeLayout.BELOW, tTextParent.id)
                        }
                    }
            val tAddCommand = it.findViewById<RelativeLayout>(R.id.ActivityPagingGenericAddCommand)

            it.removeAllViews()
            it.addView(tTextParent)
            it.addView(tActivityPagingGenericSearchAndRecyclerView)
            it.addView(tAddCommand)
        }
    }

    protected abstract fun aFinishExecutePagingGenericRelationParent()
    protected open fun oHintForParent() = ""
    override fun aFinishExecute() {
        aFinishExecutePagingGenericRelationParent()
        setCurrentParent(this.parent, TypeOriginParent.INTENT, oHintForParent())
    }

    protected fun setCurrentParent(it: TParent?, type: TypeOriginParent, hint: String) {
        if (it == null || type == TypeOriginParent.UNDEFINED) {
            this.typeOriginParent = TypeOriginParent.UNDEFINED
            return
        }
        this.parent = it
        this.typeOriginParent = type
        tTextParent.text = parent?.title ?: "├╧╤╧╤╧┤"
        tTextParent.hint = hint
        tTextParent.customHintTextColor = Color.BLACK
        fillTextSearch()
        fillAdapter()
    }

    protected abstract fun aMakeCustomViewer()
}