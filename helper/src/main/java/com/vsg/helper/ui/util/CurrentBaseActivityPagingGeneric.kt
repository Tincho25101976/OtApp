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

//    //region widget
//    private lateinit var tSearchText: AutoCompleteTextView
//    private lateinit var tSearchCommand: ImageView
//    private lateinit var tSearchSpinner: Spinner
//    private lateinit var tRecycler: RecyclerView
//    private lateinit var tAdd: ImageView
//    private lateinit var actionMenu: UICustomAlertDialogAction<TActivity, TEntity>
//    private var pagingAdapter: UIRecyclerAdapterPagingData<TEntity>? = null
//    //endregion

    //region menu
//    private var itemSelected: TEntity? = null
//    protected fun getItem(): TEntity? = itemSelected
    //endregion

    //region handler
//    var onEventMakeFilter: ((TFilter, String, PagingData<TEntity>) -> PagingData<TEntity>)? = null
//    var onEventSetCRUDForApply: ((TActivity, DBOperation) -> TCrud)? = null
//    var onEventSwipeGetViewForMenu: ((View) -> Unit)? = null
//    var onEventGetIdRelationFromIntent: (() -> Unit)? = null
//    var onEventGetListTextSearch: (() -> LiveData<List<String>>)? = null
//    var onEventGetViewAllPaging: (() -> Flow<PagingData<TEntity>>?)? = null
    //endregion

    //region relation parent
//    protected fun <TParent, TViewModelParent> getParent(type: Class<TViewModelParent>): TParent?
//            where TViewModelParent : ViewModel,
//                  TViewModelParent : IViewModelView<TParent>,
//                  TParent : IEntity {
//        val viewModelParent = makeViewModel(type)
//        val id = intent.getLongExtra(getString(R.string.msg_data), 0)
//        if (id <= 0) return null
//        val result: TParent? = viewModelParent.viewModelView(id)
//        return result
//    }
//
//    protected fun <TParent, TViewModelParent> makeViewModel(type: Class<TViewModelParent>): TViewModelParent
//            where TViewModelParent : ViewModel,
//                  TViewModelParent : IViewModelView<TParent>,
//                  TParent : IEntity {
//        return run { ViewModelProvider(this).get(type) }
//    }
    //endregion

    //region setting
//    override fun aSetContext(): CurrentBaseActivity<TViewModel, TDao, TEntity> = this

    override fun oGetViewModelGetAllTextSearch(): LiveData<List<String>> =
        currentViewModel().viewModelGetAllTextSearch()

    override fun oGetViewModelGetViewAllPaging(): Flow<PagingData<TEntity>> =
        currentViewModel().viewModelGetViewAllPaging()

//    @LayoutRes
//    protected open fun oSetSwipeMenuItems(): Int? = null
//
//    @StringRes
//    protected open fun oSetStringTitleForActionBar(): Int? = null
//    protected abstract fun aFinishExecute()

//    override fun onExecuteCreate() {
//        val customTitle = oSetStringTitleForActionBar()
//        if (customTitle != null && customTitle > 0) {
//            this.makeCustomActionbar(getString(customTitle))
//        }
//
//        onEventGetIdRelationFromIntent?.invoke()
//
//        //region widget
//        tSearchCommand = findViewById(R.id.ActivityGenericSearchCommand)
//        tSearchText = findViewById(R.id.ActivityGenericSearchText)
//        tSearchSpinner = findViewById(R.id.ActivityGenericSearchSpinner)
//        tRecycler = findViewById(R.id.ActivityGenericRecycler)
//        tAdd = findViewById(R.id.ActivityGenericSearchAdd)
//
//        tSearchCommand.apply {
//            setStatus(false)
//            setOnClickListener { search(tSearchText.toText()) }
//        }
//        fillTextSearch()
//        tRecycler.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(context())
//        }
//        tSearchSpinner.setCustomAdapterEnum(context(), typeFilter)
//        tAdd.setOnClickListener { applyCRUD(null, DBOperation.INSERT) }
//        //endregion
//
//        //region action menu:
//        actionMenu = UICustomAlertDialogAction<TActivity, TEntity>(
//            context(),
//            UICustomAlertDialogActionParameter().apply {
//                addAll(
//                    UICustomAlertDialogActionType.UPDATE,
//                    UICustomAlertDialogActionType.DELETE,
//                    UICustomAlertDialogActionType.VIEW
//                )
//
//            }).apply {
//            onEventClickItem = { type, e ->
//                when (type) {
//                    UICustomAlertDialogActionType.UPDATE -> applyCRUD(
//                        e, DBOperation.UPDATE
//                    )
//                    UICustomAlertDialogActionType.DELETE -> applyCRUD(
//                        e, DBOperation.DELETE
//                    )
//                    UICustomAlertDialogActionType.VIEW -> applyCRUD(e, DBOperation.VIEW)
//                    else -> Unit
//                }
//            }
//        }
//        //endregion
//
//        pagingAdapter = UIRecyclerAdapterPagingData<TEntity>().apply {
//            onEventClickItem = { _, id, position ->
//                val c = currentViewModel().viewModelView(id)
//                if ((c as Comparable<TEntity>) != null) {
//                    c.layoutPosition = position
//                    actionMenu.make(c)
//                }
//            }
//            onEventGetViewGroupForMenu = {
//                onEventSwipeGetViewForMenu?.invoke(it)
//            }
//            onEventGetViewForMenu = {
//                itemSelected = it
//            }
//        }
//        tRecycler.adapter = pagingAdapter
//
//        fillAdapter()
//
//        tRecycler.initSwipeToEnabled(
//            pagingAdapter!!,
//            oSetSwipeMenuItems(),
//            currentViewModel()
//        )
//        aFinishExecute()
//    }
    //endregion

    //region functional
//    protected fun fillAdapter(filter: Boolean = false, find: String = "") {
//        var data: Flow<PagingData<TEntity>>? = onEventGetViewAllPaging?.invoke()
//        if (data == null) data = oGetViewModelGetViewAllPaging()
//        lifecycleScope.launch {
//            data.collectLatest {
//                var result: PagingData<TEntity> = it
//                if (filter) {
//                    val item = tSearchSpinner.getItemEnum<TFilter>()
//                    if (item != null && find.isNotEmpty()) {
//                        result = onEventMakeFilter?.invoke(item, find, it) ?: it
//                    }
//                }
//                pagingAdapter?.submitData(result)
//            }
//        }
//    }


//    private fun search(find: String) {
//        if (find.isEmpty()) return
//        fillAdapter(true, find)
//    }

//    private fun applyCRUD(e: TEntity?, operation: DBOperation) {
//        val crud = onEventSetCRUDForApply?.invoke(context(), operation) ?: return
//        crud.apply {
//            item = e
//            onEventCRUD = {
//                if (it && e != null && e.layoutPosition > 0) pagingAdapter?.notifyItemChanged(e.layoutPosition)
//            }
//            make()
//        }
//    }

//    protected fun fillTextSearch() {
//        tSearchText.apply {
//            clearWidget(this)
//            var data = onEventGetListTextSearch?.invoke()
//            if (data == null) data = oGetViewModelGetAllTextSearch()
//            // currentViewModel().viewModelGetAllTextSearch()
//            data.observe(context(), {
//                this.setCustomAdapter(
//                    context(),
//                    it,
//                    20,
//                    true,
//                    { _, t -> search(t) },
//                    { _, t -> search(t) },
//                    tSearchCommand
//                )
//            })
//            addTextWatcher(after = { _, e ->
//                if (!e) fillAdapter() // tRecycler.adapter = pagingAdapter
//            })
//        }
//    }
    //endregion
}