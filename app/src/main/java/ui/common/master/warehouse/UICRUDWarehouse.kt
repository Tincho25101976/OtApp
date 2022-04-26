package com.vsg.ot.ui.common.master.warehouse

import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.ui.crud.UICustomCRUDViewModelRelation
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.text.CustomInputText
import com.vsg.ot.R
import common.model.master.company.MasterCompany
import common.model.master.warehouse.MasterWarehouse
import common.model.master.warehouse.MasterWarehouseDao
import common.model.master.warehouse.MasterWarehouseViewModel

@ExperimentalStdlibApi
class UICRUDWarehouse<TActivity>(
    activity: TActivity,
    operation: DBOperation,
    company: MasterCompany,
) :
    UICustomCRUDViewModelRelation<TActivity, MasterWarehouseViewModel, MasterWarehouseDao, MasterWarehouse, MasterCompany>(
        activity,
        operation,
        R.layout.dialog_master_warehouse,
        idTextParent = R.id.DialogWarehouseRelation,
        parent = company
    )
        where TActivity : CurrentBaseActivity<MasterWarehouseViewModel, MasterWarehouseDao, MasterWarehouse> {

    //region widget
    private lateinit var tName: CustomInputText
    //endregion

    override fun aGetTextParent(): String = parent.description
    override fun aGetEntityAllowDefault(): Boolean = isEntityAllowDefault<MasterWarehouse>()
    override fun oIsEntityOnlyOneDefault() = isEntityOnlyOneDefaultTrue<MasterWarehouse>()

    init {
        onEventSetInit = {
            this.tName = it.findViewById(R.id.DialogWarehouseName)
            this.viewsFont.addAll(arrayOf(tName))
        }
        onEventGetNewOrUpdateEntity = {
            val data: MasterWarehouse = it ?: MasterWarehouse()
            data.apply {
                this.valueCode = tName.text
                this.idCompany = parent.id
            }
            data
        }
        onEventSetItem = {
            tName.text = it.valueCode
        }
        onEventSetItemsForClean = {
            mutableListOf(tName)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.valueCode.isEmpty()) throw Exception("El nombre del Depósito no puede ser nulo...")
                result = true
            } catch (e: Exception) {
                message(e.message ?: "Error desconocido...")
            }
            result
        }
        onEventGetPopUpDataParameter = { p, item ->
            p?.factorHeight = 0.3
            if (item != null) {
                p?.icon = item.getDrawableShow().drawable
                p?.bitmap = item.getPictureShow()
            }
            p
        }
    }
}