package com.vsg.helper.ui.popup.select

import android.util.TypedValue
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vsg.helper.R
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.util.viewModel.IViewModelAllPaging
import com.vsg.helper.common.util.viewModel.IViewModelAllTextSearch
import com.vsg.helper.common.util.viewModel.IViewModelView
import com.vsg.helper.helper.HelperUI.Static.addTextWatcher
import com.vsg.helper.helper.HelperUI.Static.setCustomAdapter
import com.vsg.helper.helper.HelperUI.Static.setStatus
import com.vsg.helper.helper.HelperUI.Static.toText
import com.vsg.helper.ui.adapter.IDataAdapterEnum
import com.vsg.helper.ui.adapter.paging.UIRecyclerAdapterPagingData
import com.vsg.helper.ui.popup.dialog.UICustomAlertDialogParameter
import com.vsg.helper.ui.popup.dialog.UICustomAlertDialogResult
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.helper.ui.widget.spinner.CustomSpinner
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class UIAlertDialogResultEntity<TActivity, TViewModel, TEntity, TFilter>(
    val activity: TActivity,
    val currentViewModelResult: TViewModel,
//     List<TFilter>
    filters: Class<TFilter>
) :
    UICustomAlertDialogParameter(R.layout.generic_paging_and_search)
        where TActivity : BaseActivity,
              TViewModel : IViewModelAllPaging<TEntity>,
              TViewModel : IViewModelAllTextSearch,
              TViewModel : IViewModelView<TEntity>,
              TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity>,
              TFilter : IDataAdapterEnum,
              TFilter : Enum<TFilter> {
    private var dialog: UICustomAlertDialogResult<TActivity> =
        UICustomAlertDialogResult(activity, true)
    private lateinit var tSearchText: AutoCompleteTextView
    private lateinit var tSearchCommand: ImageView
    private lateinit var tSearchSpinner: CustomSpinner
    private lateinit var tRecycler: RecyclerView
    private var pagingAdapter: UIRecyclerAdapterPagingData<TEntity>? = null

    //    private var listFilter: List<TFilter> = filters
    private var itemSelect: TEntity? = null

    var onEventMakeFilter: ((TFilter, String, PagingData<TEntity>) -> PagingData<TEntity>)? = null
    var onEventClickOK: ((TEntity?) -> Unit)? = null
    var onEventGetItemAfterBind: ((TEntity) -> TEntity)? = null

    init {
        dialog.onEventSetViewContainer = {
            tSearchCommand = it.findViewById(R.id.ActivityGenericSearchCommand)
            tSearchText = it.findViewById(R.id.ActivityGenericSearchText)
            tSearchSpinner = it.findViewById(R.id.ActivityGenericSearchSpinner)
            tRecycler = it.findViewById(R.id.ActivityGenericRecycler)

            tSearchCommand.apply {
                setStatus(false)
                setOnClickListener { search(tSearchText.toText()) }
            }
            tSearchText.apply {
                clearWidget()
                currentViewModelResult.viewModelGetAllTextSearch().observe(activity) { list ->
                    this.setCustomAdapter(
                        context = activity,
                        adapter = list,
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
            tRecycler.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
            }
            tSearchSpinner.setCustomAdapterEnum(filters)

            pagingAdapter = UIRecyclerAdapterPagingData<TEntity>().apply {
                onEventClickItem = { _, id, _ ->
                    itemSelect = currentViewModelResult.viewModelView(id)
                    onEventClickOK?.invoke(itemSelect)
                    dialog.dismiss()
                }
                onEventGetItemAfterBind =
                    { t -> this@UIAlertDialogResultEntity.onEventGetItemAfterBind?.invoke(t) ?: t }
//                onEventSetTextViewTitle = { _tv ->
//                    val size = this@UIAlertDialogResultEntity.activity.resources.getInteger(R.integer.CustomAdapterTextSize).toFloat()
//                    _tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
//                    val ttt = 4
//                }
            }
            tRecycler.adapter = pagingAdapter

            fillAdapter()

//            dialog.onEventClickOK = { onEventClickOK?.invoke(itemSelect) }
        }
    }

    private fun clearWidget() {
        tSearchText.text = null
        tSearchCommand.setStatus(false)
        tRecycler.adapter = null
    }

    private fun fillAdapter() {
        activity.lifecycleScope.launch {
            currentViewModelResult.viewModelGetViewAllPaging()
                .collectLatest { pagingAdapter?.submitData(it) }
        }
    }

    private fun search(find: String) {
        if (find.isEmpty()) return
//        val item = tSearchSpinner.getItem<TFilter>() ?: return
        val item = tSearchSpinner.getItemEnum<TFilter>() ?: return
        activity.lifecycleScope.launch {
            currentViewModelResult.viewModelGetViewAllPaging().collectLatest {
                val filter: PagingData<TEntity> = onEventMakeFilter?.invoke(item, find, it) ?: it
                pagingAdapter?.submitData(filter)
            }
        }
    }

    fun make() {
        dialog.make(this)
    }
}