package com.vsg.helper.ui.util

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vsg.helper.R
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.viewModel.IViewModelView
import com.vsg.helper.common.util.viewModel.MakeGenericViewModel
import com.vsg.helper.helper.Helper.Companion.or
import com.vsg.helper.helper.Helper.Companion.then
import com.vsg.helper.helper.HelperUI.Static.addTextWatcher
import com.vsg.helper.helper.HelperUI.Static.getBitmap
import com.vsg.helper.helper.HelperUI.Static.initSwipeToEnabled
import com.vsg.helper.helper.HelperUI.Static.setCustomAdapter
import com.vsg.helper.helper.HelperUI.Static.setStatus
import com.vsg.helper.helper.HelperUI.Static.toText
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.grayScale
import com.vsg.helper.ui.adapter.IDataAdapterEnum
import com.vsg.helper.ui.adapter.paging.UIRecyclerAdapterPagingData
import com.vsg.helper.ui.crud.UICustomCRUDViewModel
import com.vsg.helper.ui.popup.action.UICustomAlertDialogAction
import com.vsg.helper.ui.popup.action.UICustomAlertDialogActionParameter
import com.vsg.helper.ui.popup.action.UICustomAlertDialogActionType
import com.vsg.helper.ui.widget.spinner.CustomSpinner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class CurrentBaseActivityPagingBase<TActivity, TViewModel, TDao, TEntity, TFilter, TCrud>(
    val type: Class<TViewModel>,
    private val typeFilter: Class<TFilter>
) : CurrentBaseActivity<TViewModel, TDao, TEntity>(
    R.layout.activity_paging_generic_and_search,
    type
)
        where TActivity : CurrentBaseActivity<TViewModel, TDao, TEntity>,
              TViewModel : MakeGenericViewModel<TDao, TEntity>,
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
    //region widget
    private lateinit var tActivityPagingGenericSearchAndRecyclerView: RelativeLayout
    private lateinit var tLayoutSearch: LinearLayout
    private lateinit var tSearchText: AutoCompleteTextView
    private lateinit var tSearchCommand: ImageView
    private lateinit var tSearchSpinner: CustomSpinner
    private lateinit var tRecycler: RecyclerView
    private lateinit var tAdd: ImageView
    private lateinit var actionMenu: UICustomAlertDialogAction<TActivity, TEntity>
    private var pagingAdapter: UIRecyclerAdapterPagingData<TEntity>? = null
    private val istAddSearchInitialized: Boolean get() = this::tAdd.isInitialized && this::tLayoutSearch.isInitialized
    private val imageAdd: Bitmap?
        @SuppressLint("UseCompatLoadingForDrawables")
        get() = this.getDrawable(R.drawable.pic_add)?.getBitmap()
    //endregion

    //region menu
    private var itemSelected: TEntity? = null
    protected fun getItem(): TEntity? = itemSelected
    //endregion

    //region handler
    var onEventMakeFilter: ((TFilter, String, PagingData<TEntity>) -> PagingData<TEntity>)? = null
    var onEventSetCRUDForApply: ((TActivity, DBOperation) -> TCrud)? = null
    var onEventSetDataUpload: ((TActivity) -> DataBase)
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
        val id = intent.getIntExtra(getString(R.string.MsgData), 0)
        if (id <= 0) return null
        return viewModelParent.viewModelView(id)
    }

    protected fun <TExtraParameter, TViewModelExtraParameter> getExtraParameter(type: Class<TViewModelExtraParameter>): TExtraParameter?
            where TViewModelExtraParameter : ViewModel,
                  TViewModelExtraParameter : IViewModelView<TExtraParameter>,
                  TExtraParameter : IEntity {
        val viewModelExtraParameter = makeViewModel(type)
        val id = intent.getIntExtra(getString(R.string.MsgExtra), 0)
        if (id <= 0) return null
        return viewModelExtraParameter.viewModelView(id)
    }
    //endregion

    //region setting
    override fun aSetContext(): CurrentBaseActivity<TViewModel, TDao, TEntity> = this

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
        tLayoutSearch = findViewById<LinearLayout?>(R.id.ActivityGenericLayoutSearch).apply {
            isEnabled = false
        }
        tSearchCommand =
            findViewById<ImageView?>(R.id.ActivityGenericSearchCommand).apply { isEnabled = false }
        tSearchText = findViewById<AutoCompleteTextView>(R.id.ActivityGenericSearchText).apply {
            isEnabled = false
        }
        tSearchSpinner = findViewById<CustomSpinner>(R.id.ActivityGenericSearchSpinner).apply {
            isEnabled = false
        }
        tRecycler = findViewById(R.id.ActivityGenericRecycler)
        tAdd = findViewById<ImageView?>(R.id.ActivityGenericSearchAdd).apply {
            setStatus(false)
        }
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
        tSearchSpinner.setCustomAdapterEnum(typeFilter).apply { }
        tAdd.setOnClickListener { applyCRUD(null, DBOperation.INSERT) }
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
            onEventClickItem = { type, e ->
                when (type) {
                    UICustomAlertDialogActionType.UPDATE -> applyCRUD(
                        e, DBOperation.UPDATE
                    )
                    UICustomAlertDialogActionType.DELETE -> applyCRUD(
                        e, DBOperation.DELETE
                    )
                    UICustomAlertDialogActionType.VIEW -> applyCRUD(e, DBOperation.VIEW)
                    else -> Unit
                }
            }
        }
        //endregion

        pagingAdapter = UIRecyclerAdapterPagingData<TEntity>().apply {
            onEventClickItem = { _, id, position ->
                val c = currentViewModel().viewModelView(id)
                if ((c as Comparable<TEntity>) != null) {
                    c.layoutPosition = position
                    actionMenu.make(c)
                }
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
            currentViewModel()
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

    private fun applyCRUD(e: TEntity?, operation: DBOperation) {
        val crud = onEventSetCRUDForApply?.invoke(context(), operation) ?: return
        crud.apply {
            item = e
            onEventCRUD = {
                if (it && e != null && e.layoutPosition > 0) pagingAdapter?.notifyItemChanged(e.layoutPosition)
            }
            make()
        }
    }
    private fun applyUploadData(e: TEntity?, operation: DBOperation) {
        val crud = onEventSetCRUDForApply?.invoke(context(), operation) ?: return
        crud.apply {
            item = e
            onEventCRUD = {
                if (it && e != null && e.layoutPosition > 0) pagingAdapter?.notifyItemChanged(e.layoutPosition)
            }
            make()
        }
    }

    protected fun fillTextSearch() {
        tSearchText.apply {
            clearWidget(this)
            var data = onEventGetListTextSearch?.invoke()
            if (data == null) data = oGetViewModelGetAllTextSearch()
            data.observe(context()) {
                this.setCustomAdapter(
                    context = context(),
                    adapter = it,
                    textSize = resources.getInteger(R.integer.CustomAdapterTextSize),
                    ignoreCase = true,
                    callbackOnItemClick = { _, t -> search(t) },
                    callbackOnKeyPressEnter = { _, t -> search(t) },
                    commandImageView = tSearchCommand
                )
            }
            addTextWatcher(after = { _, e ->
                if (!e) fillAdapter()
            })
        }
    }

    fun setEnabledAdd(result: Boolean) {
        if (!istAddSearchInitialized) return
        tAdd.setStatus(result, 75u)
        arrayOf(tAdd, tSearchSpinner, tSearchText, tLayoutSearch, tSearchCommand)
            .forEach { it.isEnabled = result }
        if (this.imageAdd == null) return
        tAdd.setImageBitmap(result then imageAdd or imageAdd!!.grayScale())
    }
    //endregion
}