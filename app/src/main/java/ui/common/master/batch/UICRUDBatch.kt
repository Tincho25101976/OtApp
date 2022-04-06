package com.vsg.ot.ui.common.master.batch

import android.view.Gravity
import com.vsg.helper.common.format.FormatDateString
import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.helper.date.HelperDate
import com.vsg.helper.helper.date.HelperDate.Companion.addDay
import com.vsg.helper.helper.date.HelperDate.Companion.toDateString
import com.vsg.helper.ui.crud.UICustomCRUDViewModelRelation
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.text.CustomInputText
import com.vsg.ot.R
import common.model.master.batch.MasterBatch
import common.model.master.batch.MasterBatchDao
import common.model.master.batch.MasterBatchViewModel
import common.model.master.item.MasterItem


@ExperimentalStdlibApi
class UICRUDBatch<TActivity>(
    activity: TActivity,
    operation: DBOperation,
    product: MasterItem
) :
    UICustomCRUDViewModelRelation<TActivity, MasterBatchViewModel, MasterBatchDao, MasterBatch, MasterItem>(
        activity,
        operation,
        R.layout.dialog_batch,
        idTextParent = R.id.DialogBatchRelation,
        parent = product
    )
        where TActivity : CurrentBaseActivity<MasterBatchViewModel, MasterBatchDao, MasterBatch> {

    //region widget
    private lateinit var tDueDate: CustomInputText
    private lateinit var tCreateDate: CustomInputText
    private lateinit var tReceiverQty: CustomInputText
    private val formatDate = FormatDateString.CREATE_DATE
    //endregion

    override fun aGetTextParent(): String = parent.description

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
            val data: MasterBatch = it ?: MasterBatch()
            data.apply {
                this.dueDate = tDueDate.date
                this.createDate = tCreateDate.date
                this.receiverQty = tReceiverQty.toDouble()
                this.idItem = parent.id
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
                p?.icon = item.getDrawableShow().drawable
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