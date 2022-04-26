package com.vsg.ot.ui.common.master.product

import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.helper.exception.HelperException.Companion.throwException
import com.vsg.helper.ui.crud.UICustomCRUDViewModelRelation
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.spinner.CustomSpinner
import com.vsg.helper.ui.widget.text.CustomInputText
import com.vsg.helper.util.unit.type.TypeUnit
import com.vsg.ot.R
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem
import common.model.master.item.MasterItemDao
import common.model.master.item.MasterItemViewModel
import common.model.master.item.type.TypePlant
import common.model.master.item.type.TypeProduct

@ExperimentalStdlibApi
class UICRUDItem<TActivity>(
    activity: TActivity,
    operation: DBOperation,
    viewModel: MasterItemViewModel,
    val company: MasterCompany
) :
    UICustomCRUDViewModelRelation<TActivity, MasterItemViewModel, MasterItemDao, MasterItem, MasterCompany>(
        activity,
        operation,
        R.layout.dialog_master_item,
        viewModel,
        idTextParent = R.id.DialogProductRelation,
        parent = company
    )
        where TActivity : CurrentBaseActivity<MasterItemViewModel, MasterItemDao, MasterItem> {

    //region widget
    private lateinit var tName: CustomInputText
    private lateinit var tShellLife: CustomInputText
    private lateinit var tShellLifeAlert: CustomInputText
    private lateinit var tUnit: CustomSpinner
    private lateinit var tPlant: CustomSpinner
    private lateinit var tProduct: CustomSpinner
    //endregion

    override fun aGetTextParent(): String = parent.description
    override fun aGetEntityAllowDefault(): Boolean = isEntityAllowDefault<MasterItem>()

    init {
        onEventSetInit = { it ->
            this.tName = it.findViewById(R.id.DialogItemName)
            this.tShellLife = it.findViewById(R.id.DialogItemShellLife)
            this.tShellLifeAlert = it.findViewById(R.id.DialogItemShellLifeAlert)
            this.tPlant = it.findViewById<CustomSpinner?>(R.id.DialogItemTypePlant).apply {
                setCustomAdapterEnum(TypePlant::class.java)
            }
            this.tProduct = it.findViewById<CustomSpinner?>(R.id.DialogItemTypeProduct).apply {
                setCustomAdapterEnum(TypeProduct::class.java)
            }
            this.tUnit = it.findViewById<CustomSpinner>(R.id.DialogItemTypeUnit).apply {
                setCustomAdapterEnum(TypeUnit::class.java)
            }

            viewsFont.addAll(arrayOf(tName, tShellLife, tShellLifeAlert, tPlant, tProduct, tUnit))
        }
        onEventGetNewOrUpdateEntity = {
            val data = it ?: MasterItem()
            data.apply {
                this.typeUnit = tUnit.getItemEnumOrDefault() ?: TypeUnit.UNDEFINED
                this.typePlant = tPlant.getItemEnumOrDefault() ?: TypePlant.UNDEFINED
                this.typeProduct = tProduct.getItemEnumOrDefault() ?: TypeProduct.UNDEFINED
                this.valueCode = tName.text
                this.shellLife = tShellLife.toInt()
                this.shellLifeAlert = tShellLifeAlert.toInt()
                this.idCompany = this@UICRUDItem.company.id
            }
            data
        }
        onEventSetItem = {
            tName.text = it.valueCode
            tShellLife.int = it.shellLife
            tShellLifeAlert.int = it.shellLifeAlert
            tUnit.setItemEnum(it.typeUnit)
            tPlant.setItemEnum(it.typePlant)
            tProduct.setItemEnum(it.typeProduct)
        }
        onEventSetItemsForClean = {
            mutableListOf(tName, tShellLife, tShellLifeAlert, tUnit, tPlant, tProduct)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.idCompany <= 0) "No se ha establecido la Empresa".throwException()
                if (item.valueCode.isEmpty()) "El código del producto no puede ser una cadena de longitud cero".throwException()
                if(item.description.isEmpty()) "La descripción del producto no puede ser una cadena de longitud cero".throwException()
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
}