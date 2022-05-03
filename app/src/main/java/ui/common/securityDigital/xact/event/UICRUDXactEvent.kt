package com.vsg.ot.ui.common.securityDigital.xact.event

import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.ui.crud.UICustomCRUDViewModel
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.text.CustomInputText
import com.vsg.ot.R
import com.vsg.ot.common.model.securityDialog.xact.event.XactEvent
import com.vsg.ot.common.model.securityDialog.xact.event.XactEventDao
import com.vsg.ot.common.model.securityDialog.xact.event.XactEventViewModel

@ExperimentalStdlibApi
class UICRUDXactEvent<TActivity>(activity: TActivity, operation: DBOperation) :
    UICustomCRUDViewModel<TActivity, XactEventViewModel, XactEventDao, XactEvent>(
        activity,
        operation,
        R.layout.dialog_xact_process
    )
        where TActivity : CurrentBaseActivity<XactEventViewModel, XactEventDao, XactEvent> {

    //region widget
    private lateinit var tValueCode: CustomInputText
    //endregion

    override fun aGetEntityAllowDefault(): Boolean = isEntityAllowDefault<XactEvent>()

    init {
        onEventSetInit = {
            this.tValueCode = it.findViewById(R.id.DialogXactProcessValueCode)
        }
        onEventGetNewOrUpdateEntity = {
            val data = it ?: XactEvent()
            data.apply {
                this.valueCode = tValueCode.text
            }
            data
        }
        onEventSetItem = {
            tValueCode.text = it.valueCode
        }
        onEventSetItemsForClean = {
            mutableListOf(tValueCode)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.valueCode.isEmpty()) throw Exception("El nombre del proceso no puede ser nulo...")
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
}