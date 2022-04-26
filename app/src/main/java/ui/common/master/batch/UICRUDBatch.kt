package com.vsg.ot.ui.common.master.batch

import com.vsg.helper.common.format.FormatDateString
import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.helper.date.HelperDate.Companion.addDay
import com.vsg.helper.helper.date.HelperDate.Companion.now
import com.vsg.helper.helper.date.HelperDate.Companion.nowDate
import com.vsg.helper.helper.date.HelperDate.Companion.toDateString
import com.vsg.helper.helper.exception.HelperException.Companion.throwException
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
        R.layout.dialog_master_batch,
        idTextParent = R.id.DialogBatchRelation,
        parent = product
    )
        where TActivity : CurrentBaseActivity<MasterBatchViewModel, MasterBatchDao, MasterBatch> {

    //region widget
    private lateinit var tValueCode: CustomInputText
    private lateinit var tDueDate: CustomInputText
    private lateinit var tCreateDate: CustomInputText
    private lateinit var tReceiverQty: CustomInputText
    private val formatDate = FormatDateString.CREATE_DATE
    //endregion

    override fun aGetTextParent(): String = parent.description
    override fun aGetEntityAllowDefault(): Boolean = isEntityAllowDefault<MasterBatch>()

    init {
        onEventSetInit = {
            this.tValueCode = it.findViewById<CustomInputText>(R.id.DialogBatchValueCode)
                .apply { setOnlyTextUpper() }
            this.tCreateDate = it.findViewById<CustomInputText>(R.id.DialogBatchCreateDate).apply {
                customFormatDate = this@UICRUDBatch.formatDate
                setDatePicker(activity) { setDueDate() }
                date = nowDate()
            }
            this.tDueDate = it.findViewById<CustomInputText>(R.id.DialogBatchDueDate).apply {
                customFormatDate = this@UICRUDBatch.formatDate
                setDatePicker(activity)
                date = nowDate()
            }
            this.tReceiverQty = it.findViewById(R.id.DialogBatchReceiverQty)
        }
        onEventGetNewOrUpdateEntity = {
            val data: MasterBatch = it ?: MasterBatch()
            data.apply {
                this.valueCode = tValueCode.text
                this.dueDate = tDueDate.date
                this.createDate = tCreateDate.date ?: nowDate()
                this.receiverQty = tReceiverQty.toDouble()
                this.idItem = parent.id
                this.idCompany = parent.idCompany
            }
            data
        }
        onEventSetItem = {
            tValueCode.text = it.valueCode
            tCreateDate.date = it.createDate
            tDueDate.date = it.dueDate
            tReceiverQty.double = it.receiverQty
        }
        onEventSetItemsForClean = {
            mutableListOf(tValueCode, tDueDate, tReceiverQty, tCreateDate)
        }
        onEventSetParametersForInsert = {
            tCreateDate.date = nowDate()
            tDueDate.date = nowDate().addDay(parent.shellLife)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.valueCode.isEmpty()) "El numero del batch no puede ser una cadena de longitud cero".throwException()
                if (item.receiverQty < 0.0) "La recepción debe ser un numero positivo".throwException()
                if (item.dueDate == null) "La fecha de vencimiento no puede ser nula".throwException()
                if (item.createDate >= item.dueDate) {
                    "La fecha de creación debe ser inferior a ${
                        item.dueDate.toDateString(formatDate)
                    }".throwException()
                }
                result = true
            } catch (e: Exception) {
                message(e.message ?: "Error desconocido...")
            }
            result
        }
        onEventGetPopUpDataParameter = { p, item ->
            p?.factorHeight = 0.45
            if (item != null && p != null) {
                p.icon = item.getDrawableShow().drawable
                p.bitmap = item.getPictureShow()
                p.toHtml = item.reference()
            }
            p
        }
    }

    private fun setDueDate() {
        val day = tCreateDate.toDate(formatDate)
        if (day != null) tDueDate.date = day.addDay(this@UICRUDBatch.parent.shellLife)
    }
}