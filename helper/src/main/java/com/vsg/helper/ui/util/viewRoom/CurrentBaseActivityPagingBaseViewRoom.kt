package com.vsg.helper.ui.util.viewRoom

import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vsg.helper.R
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.viewRoom.IEntityViewRoom
import com.vsg.helper.common.util.dao.viewRoom.IGenericDaoPagingRelationViewRoom
import com.vsg.helper.common.util.viewModel.IViewModelView
import com.vsg.helper.common.util.viewModel.viewRoom.MakeGenericViewModelViewRoom
import com.vsg.helper.helper.HelperUI.Static.addTextWatcher
import com.vsg.helper.helper.HelperUI.Static.initSwipeToEnabled
import com.vsg.helper.helper.HelperUI.Static.setCustomAdapter
import com.vsg.helper.helper.HelperUI.Static.setStatus
import com.vsg.helper.helper.HelperUI.Static.toText
import com.vsg.helper.ui.adapter.IDataAdapterEnum
import com.vsg.helper.ui.adapter.paging.UIRecyclerAdapterPagingData
import com.vsg.helper.ui.popup.action.UICustomAlertDialogAction
import com.vsg.helper.ui.popup.action.UICustomAlertDialogActionParameter
import com.vsg.helper.ui.popup.action.UICustomAlertDialogActionType
import com.vsg.helper.ui.widget.spinner.CustomSpinner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class CurrentBaseActivityPagingBaseViewRoom<TActivity, TViewModel, TDao, TEntity, TViewRoom, TFilter>(
    val type: Class<TViewModel>,
    private val typeFilter: Class<TFilter>
) : CurrentBaseActivityViewRoom<TViewModel, TDao, TEntity, TViewRoom>(
    R.layout.activity_paging_generic_and_search,
    type
)
        where TActivity : CurrentBaseActivityViewRoom<TViewModel, TDao, TEntity, TViewRoom>,
              TViewModel : MakeGenericViewModelViewRoom<TDao, TEntity, TViewRoom>,
              TDao : IGenericDaoPagingRelationViewRoom<TEntity, TViewRoom>,
              TEntity : IEntityViewRoom<TEntity>,
              TFilter : IDataAdapterEnum,
              TFilter : Enum<TFilter>,
              TViewRoom : IEntity {
    //region widget
    private lateinit var tActivityPagingGenericSearchAndRecyclerView: RelativeLayout
    private lateinit var tSearchText: AutoCompleteTextView
    private lateinit var tSearchCommand: ImageView
    private lateinit var tSearchSpinner: CustomSpinner
    private lateinit var tRecycler: RecyclerView

    //    private lateinit var tAdd: ImageView
    private lateinit var actionMenu: UICustomAlertDialogAction<TActivity, TEntity>
    private var pagingAdapter: UIRecyclerAdapterPagingData<TEntity>? = null
    //endregion

    //region menu
    private var itemSelected: TEntity? = null
    protected fun getItem(): TEntity? = itemSelected
    //endregion

    //region handler
    var onEventMakeFilter: ((TFilter, String, PagingData<TEntity>) -> PagingData<TEntity>)? = null
    var onEventSwipeGetViewForMenu: ((View) -> Unit)? = null
    var onEventGetIdRelationFromIntent: (() -> Unit)? = null
    var onEventGetListTextSearch: (() -> LiveData<List<String>>)? = null
    var onEventGetViewAllPaging: (() -> Flow<PagingData<TEntity>>?)? = null
    //endregion

    //region relation parent
    protected fun <TParent, TViewModelParent> getParent(type: Class<TViewModelParent>): TParent?
            where TViewModelParent : ViewModel,
                  TViewModelParent : IViewModelView<TParent>,
                  TParent : IEntity {
        val viewModelParent = makeViewModel(type)
        val id = intent.getIntExtra(getString(R.string.msg_data), 0)
        if (id <= 0) return null
        return viewModelParent.viewModelView(id)
    }

    protected fun <TExtraParameter, TViewModelExtraParameter> getExtraParameter(type: Class<TViewModelExtraParameter>): TExtraParameter?
            where TViewModelExtraParameter : ViewModel,
                  TViewModelExtraParameter : IViewModelView<TExtraParameter>,
                  TExtraParameter : IEntity {
        val viewModelExtraParameter = makeViewModel(type)
        val id = intent.getIntExtra(getString(R.string.msg_extra), 0)
        if (id <= 0) return null
        return viewModelExtraParameter.viewModelView(id)
    }
    //endregion

    //region setting
    override fun aSetContext(): CurrentBaseActivityViewRoom<TViewModel, TDao, TEntity, TViewRoom> =
        this

    protected open fun oGetViewModelGetAllTextSearch(): LiveData<List<String>> =
        MutableLiveData()

    protected open fun oGetViewModelGetViewAllPaging(): Flow<PagingData<TEntity>>? = null

    @LayoutRes
    protected open fun oSetSwipeMenuItems(): Int? = null

    @StringRes
    protected open fun oSetStringTitleForActionBar(): Int? = null
    protected abstract fun aFinishExecute()

    override fun onExecuteCreate() {
        val customTitle = oSetStringTitleForActionBar()
        if (customTitle != null && customTitle > 0) {
            this.makeCustomActionbar(getString(customTitle))
        }

        onEventGetIdRelationFromIntent?.invoke()

        //region widget
        tSearchCommand = findViewById(R.id.ActivityGenericSearchCommand)
        tSearchText = findViewById(R.id.ActivityGenericSearchText)
        tSearchSpinner = findViewById(R.id.ActivityGenericSearchSpinner)
        tRecycler = findViewById(R.id.ActivityGenericRecycler)
        //tAdd =
        findViewById<ImageView>(R.id.ActivityGenericSearchAdd).visibility = View.GONE
        tActivityPagingGenericSearchAndRecyclerView =
            findViewById(R.id.ActivityPagingGenericSearchAndRecyclerView)

        tSearchCommand.apply {
            setStatus(false)
            setOnClickListener { search(tSearchText.toText()) }
        }
        fillTextSearch()
        tRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context())
        }
        //tSearchSpinner.setCustomAdapterEnum(context(), typeFilter)
        tSearchSpinner.setCustomAdapterEnum(typeFilter)
//        tAdd.setOnClickListener { applyCRUD(null, DBOperation.INSERT) }
        //endregion

        //region action menu:
        actionMenu = UICustomAlertDialogAction<TActivity, TEntity>(
            context(),
            UICustomAlertDialogActionParameter().apply {
                addAll(
                    UICustomAlertDialogActionType.UPDATE,
                    UICustomAlertDialogActionType.DELETE,
                    UICustomAlertDialogActionType.VIEW
                )

            }).apply {
            onEventClickItem = { type, _ ->
                when (type) {
//                    UICustomAlertDialogActionType.UPDATE -> applyCRUD(
//                        e, DBOperation.UPDATE
//                    )
//                    UICustomAlertDialogActionType.DELETE -> applyCRUD(
//                        e, DBOperation.DELETE
//                    )
                    UICustomAlertDialogActionType.VIEW -> Unit //applyCRUD(e, DBOperation.VIEW)
                    else -> Unit
                }
            }
        }
        //endregion

        pagingAdapter = UIRecyclerAdapterPagingData<TEntity>().apply {
            onEventClickItem = { _, id, position ->
//                val c = currentViewModel().viewModelView(id)
//                if ((c as Comparable<TEntity>) != null) {
//                    c.layoutPosition = position
//                    actionMenu.make(c)
//                }
            }
            onEventGetViewGroupForMenu = {
                onEventSwipeGetViewForMenu?.invoke(it)
            }
            onEventGetViewForMenu = {
                itemSelected = it
            }
        }
        tRecycler.adapter = pagingAdapter

        fillAdapter()

        tRecycler.initSwipeToEnabled(
            pagingAdapter!!,
            oSetSwipeMenuItems(),
            null
        )
        aFinishExecute()
    }
    //endregion

    //region functional
    protected fun fillAdapter(filter: Boolean = false, find: String = "") {
        var data: Flow<PagingData<TEntity>>? = onEventGetViewAllPaging?.invoke()
        if (data == null) data = oGetViewModelGetViewAllPaging()
        if (data == null) return
        lifecycleScope.launch {
            data.collectLatest {
                var result: PagingData<TEntity> = it
                if (filter) {
                    val item = tSearchSpinner.getItemEnum<TFilter>()
                    if (item != null && find.isNotEmpty()) {
                        result = onEventMakeFilter?.invoke(item, find, it) ?: it
                    }
                }
                pagingAdapter?.submitData(result)
            }
        }
    }

    private fun search(find: String) {
        if (find.isEmpty()) return
        fillAdapter(true, find)
    }

    protected fun fillTextSearch() {
        tSearchText.apply {
            clearWidget(this)
            var data = onEventGetListTextSearch?.invoke()
            if (data == null) data = oGetViewModelGetAllTextSearch()
            data.observe(context(), {
                this.setCustomAdapter(
                    context = context(),
                    adapter = it,
                    textSize = 20,
                    ignoreCase = true,
                    callbackOnItemClick = { _, t -> search(t) },
                    callbackOnKeyPressEnter = { _, t -> search(t) },
                    commandImageView = tSearchCommand
                )
            })
            addTextWatcher(after = { _, e ->
                if (!e) fillAdapter()
            })
        }
    }
    //endregion
}