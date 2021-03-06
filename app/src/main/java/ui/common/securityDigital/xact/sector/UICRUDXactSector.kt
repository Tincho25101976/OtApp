package com.vsg.ot.ui.common.securityDigital.xact.sector

import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.ui.crud.UICustomCRUDViewModel
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.text.CustomInputText
import com.vsg.ot.R
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSector
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSectorDao
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSectorViewModel

@ExperimentalStdlibApi
class UICRUDXactSector<TActivity>(activity: TActivity, operation: DBOperation) :
    UICustomCRUDViewModel<TActivity, XactSectorViewModel, XactSectorDao, XactSector>(
        activity,
        operation,
        R.layout.dialog_xact_sector
    )
        where TActivity : CurrentBaseActivity<XactSectorViewModel, XactSectorDao, XactSector> {

    //region widget
    private lateinit var tValueCode: CustomInputText
    //endregion

    override fun aGetEntityAllowDefault(): Boolean = isEntityAllowDefault<XactSector>()

    init {
        onEventSetInit = {
            this.tValueCode = it.findViewById(R.id.DialogXactSectorValueCode)
        }
        onEventGetNewOrUpdateEntity = {
            val data = it ?: XactSector()
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
                if (item.valueCode.isEmpty()) throw Exception("El nombre del sector no puede ser nulo...")
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