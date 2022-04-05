package com.vsg.agendaandpublication.ui.common.itemOperation.warehouse

import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse.Warehouse
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse.WarehouseDao
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse.WarehouseViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.company.Company
import com.vsg.utilities.common.operation.DBOperation
import com.vsg.utilities.ui.crud.UICustomCRUDViewModelRelation
import com.vsg.utilities.ui.util.CurrentBaseActivity
import com.vsg.utilities.ui.widget.text.CustomInputText

@ExperimentalStdlibApi
class UICRUDWarehouse<TActivity>(
    activity: TActivity,
    operation: DBOperation,
    company: Company,
) :
    UICustomCRUDViewModelRelation<TActivity, WarehouseViewModel, WarehouseDao, Warehouse, Company>(
        activity,
        operation,
        R.layout.dialog_warehouse,
        idTextParent = R.id.DialogWarehouseRelation,
        parent = company
    )
        where TActivity : CurrentBaseActivity<WarehouseViewModel, WarehouseDao, Warehouse> {

    //region widget
    private lateinit var tName: CustomInputText
    private lateinit var tPrefix: CustomInputText
    private lateinit var tLocation: CustomInputText
    //endregion

    override fun aGetTextParent(): String = parent.name
    override fun oIsEntityOnlyOneDefault() = isEntityOnlyOneDefaultTrue<Warehouse>()

    init {
        onEventSetInit = {
            this.tName = it.findViewById(R.id.DialogWarehouseName)
            this.tPrefix = it.findViewById(R.id.DialogWarehousePrefix)
            this.tLocation = it.findViewById(R.id.DialogWarehouseLocation)
        }
        onEventGetNewOrUpdateEntity = {
            val data: Warehouse = it ?: Warehouse()
            data.apply {
                this.name = tName.text
                this.prefix = tPrefix.text
                this.location = tLocation.text
                this.idCompany = parent.id
            }
            data
        }
        onEventSetItem = {
            tName.text = it.name
            tPrefix.text = it.prefix
            tLocation.text = it.location
        }
        onEventSetItemsForClean = {
            mutableListOf(tName, tPrefix, tLocation)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.name.isEmpty()) throw Exception("El nombre del Depósito no puede ser nulo...")
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
                p?.icon = item.getDrawableShow()
                p?.bitmap = item.getPictureShow()
            }
            p
        }
    }
}