package com.vsg.helper.ui.util

import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vsg.helper.R
import com.vsg.helper.common.adapter.IRecyclerAdapter
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IReference
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.viewModel.IViewModelAllById
import com.vsg.helper.common.util.viewModel.MakeGenericViewModel
import com.vsg.helper.ui.adapter.UIRecyclerAdapter
import com.vsg.helper.ui.crud.UICustomCRUDAddItem
import com.vsg.helper.ui.popup.action.UICustomAlertDialogAction
import com.vsg.helper.ui.popup.action.UICustomAlertDialogActionParameter
import com.vsg.helper.ui.popup.action.UICustomAlertDialogActionType

abstract class CurrentBaseActivityAddItemGeneric<TActivity, TViewModel, TDao, TEntity, TEntityParent, TCustomCrud>(
    type: Class<TViewModel>
) :
    CurrentBaseActivity<TViewModel, TDao, TEntity>(
        R.layout.activity_generic_for_add_item,
        type
    )
        where TActivity : CurrentBaseActivity<TViewModel, TDao, TEntity>,
              TViewModel : MakeGenericViewModel<TDao, TEntity>,
              TViewModel : IViewModelAllById<TEntity>,
              TDao : IGenericDao<TEntity>,
              TEntity : ItemBase,
              TEntity : IResultRecyclerAdapter,
              TEntityParent : IReference,
              TEntityParent : IEntity,
              TCustomCrud : UICustomCRUDAddItem<TActivity, TViewModel, TDao, TEntity, TEntityParent> {
    //region widget
    private lateinit var tCardViewText: TextView
    private lateinit var tCardViewPicture: ImageView
    private lateinit var tRecycler: RecyclerView
    private lateinit var tAdd: ImageView
    private var actionMenu: UICustomAlertDialogAction<TActivity, TEntity>? = null
    protected var parent: TEntityParent? = null
    //endregion

    //region handler
    protected var onEventCreateUICustomCRUD: ((DBOperation) -> TCustomCrud)? = null
    protected var onEventCreateActionMenu: ((UICustomAlertDialogActionParameter) -> UICustomAlertDialogAction<TActivity, TEntity>)? =
        null
    protected var onEventGetParent: (() -> TEntityParent?)? = null
    protected var onEventSetTextViewTitleRecyclerAdapter: ((TextView) -> Unit)? = null
    //endregion

    override fun onExecuteCreate() {
        this.parent = onEventGetParent?.invoke()
        if (parent == null) finish()

        //region widget
        tCardViewPicture = findViewById(R.id.ActivityGenericForAddItemCardViewPicture)
        tCardViewText = findViewById(R.id.ActivityGenericForAddItemCardViewText)
        tRecycler = findViewById(R.id.ActivityGenericForAddItemRecycler)
        tAdd = findViewById(R.id.ActivityGenericForAddItemAdd)

        tCardViewPicture.apply {
            val p = this@CurrentBaseActivityAddItemGeneric.parent
            if (p != null) {
                if (p.isBitmap) setImageBitmap(p.getPictureShow())
                else setImageBitmap(BitmapFactory.decodeResource(resources, p.getDrawableShow()))
            }
        }
        tCardViewText.apply {
            text = this@CurrentBaseActivityAddItemGeneric.parent?.reference()
        }
        tRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context())
        }
        tAdd.apply {
            setOnClickListener {
                applyCRUD(null, DBOperation.INSERT)
            }
        }
        //endregion

        //region action menu:
        val parameter = UICustomAlertDialogActionParameter().apply {
            addAll(
                UICustomAlertDialogActionType.UPDATE,
                UICustomAlertDialogActionType.DELETE,
                UICustomAlertDialogActionType.VIEW
            )
        }
        actionMenu = onEventCreateActionMenu?.invoke(parameter)
        if (actionMenu != null) {
            actionMenu?.onEventClickItem = { type, e ->
                when (type) {
                    UICustomAlertDialogActionType.UPDATE -> applyCRUD(
                        e, DBOperation.UPDATE
                    )
                    UICustomAlertDialogActionType.DELETE -> applyCRUD(
                        e, DBOperation.DELETE
                    )
                    UICustomAlertDialogActionType.VIEW -> applyCRUD(
                        e, DBOperation.VIEW
                    )
                    else -> Unit
                }
            }
//            }
        }
        //endregion

        currentViewModel().viewModelViewAll(parent?.id!!)?.observe(context(), { setAdapter(it) })
    }

    //region functional
    private fun setAdapter(it: List<TEntity>) {
        tRecycler.adapter = null
        if (!it.any()) return
        tRecycler.adapter =
            UIRecyclerAdapter<IRecyclerAdapter>(it.map { s -> s.getRecyclerAdapter() }).apply {
                onEventClickItem = { _, id ->
                    val c: TEntity? = currentViewModel().viewModelView(id)
                    if (c != null) actionMenu?.make(c)
                }
                onEventSetTextViewTitle = {
                    onEventSetTextViewTitleRecyclerAdapter?.invoke(it)
                }
            }
    }

    private fun applyCRUD(e: TEntity?, operation: DBOperation) {
        val crud = onEventCreateUICustomCRUD?.invoke(operation) ?: return
        crud.apply {
            item = e
            make()
        }
    }
    //endregion
}