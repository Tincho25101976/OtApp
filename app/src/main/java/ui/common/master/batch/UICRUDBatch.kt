package com.vsg.agendaandpublication.ui.common.itemOperation.batch

import android.view.Gravity
import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemOperation.batch.Batch
import com.vsg.agendaandpublication.common.model.itemOperation.batch.BatchDao
import com.vsg.agendaandpublication.common.model.itemOperation.batch.BatchViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.price.Price
import com.vsg.agendaandpublication.common.model.itemProduct.product.Product
import com.vsg.utilities.common.operation.DBOperation
import com.vsg.utilities.helper.date.HelperDate
import com.vsg.utilities.helper.date.HelperDate.Companion.addDay
import com.vsg.utilities.helper.date.HelperDate.Companion.toDateString
import com.vsg.utilities.ui.crud.UICustomCRUDViewModelRelation
import com.vsg.utilities.ui.util.CurrentBaseActivity
import com.vsg.utilities.ui.widget.text.CustomInputText

@ExperimentalStdlibApi
class UICRUDBatch<TActivity>(
    activity: TActivity,
    operation: DBOperation,
    product: Product
) :
    UICustomCRUDViewModelRelation<TActivity, BatchViewModel, BatchDao, Batch, Product>(
        activity,
        operation,
        R.layout.dialog_batch,
        idTextParent = R.id.DialogBatchRelation,
        parent = product
    )
        where TActivity : CurrentBaseActivity<BatchViewModel, BatchDao, Batch> {

    //region widget
    private lateinit var tDueDate: CustomInputText
    private lateinit var tCreateDate: CustomInputText
    private lateinit var tReceiverQty: CustomInputText
    private val formatDate = Price().formatDate
    //endregion

    override fun aGetTextParent(): String = parent.name

    init {
        onEventSetInit = {
            this.tCreateDate = it.findViewById<CustomInputText>(R.id.DialogBatchCreateDate).apply {
                customFormatDate = this@UICRUDBatch.formatDate
                setDatePicker(activity) { setDueDate() }
                date = HelperDate.now()
                gravity = Gravity.CENTER_HORIZONTAL
            }
            this.tDueDate = it.findViewById<CustomInputText>(R.id.DialogBatchDueDate).apply {
                customFormatDate = this@UICRUDBatch.formatDate
                setDatePicker(activity)
                date = HelperDate.now()
                gravity = Gravity.CENTER_HORIZONTAL
            }
            this.tReceiverQty = it.findViewById(R.id.DialogBatchReceiverQty)
        }
        onEventGetNewOrUpdateEntity = {
            val data: Batch = it ?: Batch()
            data.apply {
                this.dueDate = tDueDate.date
                this.createDate = tCreateDate.date
                this.receiverQty = tReceiverQty.toDouble()
                this.idProduct = parent.id
                this.idCompany = parent.idCompany
            }
            data
        }
        onEventSetItem = {
            tCreateDate.date = it.createDate
            tDueDate.date = it.dueDate
            tReceiverQty.double = it.receiverQty
        }
        onEventSetItemsForClean = {
            mutableListOf(tDueDate, tReceiverQty, tCreateDate)
        }
        onEventSetParametersForInsert = {
            tCreateDate.date = HelperDate.now()
            tDueDate.date = HelperDate.now().addDay(parent.shellLife)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.receiverQty < 0.0) throw Exception("La recepción debe ser un numero positivo...")
                if (item.createDate == null) throw Exception("La fecha de vencimiento no puede ser nula...")
                if (item.dueDate == null) throw Exception("La fecha de vencimiento no puede ser nula...")
                if (item.createDate!! <= item.dueDate) {
                    throw Exception(
                        "La fecha de creación debe ser inferior a ${
                            item.dueDate.toDateString(formatDate)
                        }..."
                    )
                }
                result = true
            } catch (e: Exception) {
                message(e.message ?: "Error desconocido...")
            }
            result
        }
        onEventGetPopUpDataParameter = { p, item ->
            p?.factorHeight = 0.25
            if (item != null) {
                p?.icon = item.getDrawableShow()
                p?.bitmap = item.getPictureShow()
            }
            p
        }
    }

    private fun setDueDate() {
        val day = tCreateDate.toDate(formatDate)
        if (day != null) tDueDate.date = day.addDay(this@UICRUDBatch.parent.shellLife)
    }
}