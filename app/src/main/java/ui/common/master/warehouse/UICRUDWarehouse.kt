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
    private lateinit var tPrefix: CustomInputText
    private lateinit var tLocation: CustomInputText
    //endregion

    override fun aGetTextParent(): String = parent.description
    override fun oIsEntityOnlyOneDefault() = isEntityOnlyOneDefaultTrue<MasterWarehouse>()

    init {
        onEventSetInit = {
            this.tName = it.findViewById(R.id.DialogWarehouseName)
            this.tPrefix = it.findViewById(R.id.DialogWarehousePrefix)
            this.tLocation = it.findViewById(R.id.DialogWarehouseLocation)
        }
        onEventGetNewOrUpdateEntity = {
            val data: MasterWarehouse = it ?: MasterWarehouse()
            data.apply {
                this.description = tName.text
                this.prefix = tPrefix.text
                this.location = tLocation.text
                this.idCompany = parent.id
            }
            data
        }
        onEventSetItem = {
            tName.text = it.description
            tPrefix.text = it.prefix
            tLocation.text = it.location
        }
        onEventSetItemsForClean = {
            mutableListOf(tName, tPrefix, tLocation)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.description.isEmpty()) throw Exception("El nombre del Depósito no puede ser nulo...")
                if (item.prefix.isEmpty()) throw Exception("La abreviatura del Depósito no puede ser nula...")
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