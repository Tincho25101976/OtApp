package com.vsg.ot.ui.common.master.product

import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.helper.HelperEnum.Companion.getListEnum
import com.vsg.helper.ui.crud.UICustomCRUDViewModelRelation
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.spinner.CustomSpinner
import com.vsg.helper.ui.widget.text.CustomInputText
import com.vsg.helper.util.helper.HelperUtil.Companion.toUnit
import com.vsg.helper.util.unit.type.TypeUnit
import com.vsg.ot.R
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem
import common.model.master.item.MasterItemDao
import common.model.master.item.MasterItemViewModel
import com.vsg.helper.util.unit.Unit as MasterUnit

@ExperimentalStdlibApi
class UICRUDProduct<TActivity>(
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
    private lateinit var tUnit: CustomSpinner
    private var unit: MasterUnit? = null
    //endregion

    override fun aGetTextParent(): String = parent.description

    init {
        onEventSetInit = { it ->
            this.tName = it.findViewById(R.id.DialogProductName)
//            this.tUnit = it.findViewById<CustomSpinner>(R.id.DialogProductUnit).apply {
//                setCustomAdapter(, { ce -> unit = ce })
//            }
            this.tUnit = it.findViewById<CustomSpinner>(R.id.DialogProductUnit).apply {
                val dataSource: List<MasterUnit> =
                    TypeUnit::class.java.getListEnum().map { c -> c.toUnit() }
                setCustomAdapter(dataSource, { c -> unit = c })
            }
        }
        onEventGetNewOrUpdateEntity = {
            val data = it ?: MasterItem()
            data.apply {
                this.description = tName.text
                this.idCompany = this@UICRUDProduct.company.id
            }
            data
        }
        onEventSetItem = {
            tName.text = it.description
            tUnit.setItem<MasterUnit>(it.unit)
        }
        onEventSetItemsForClean = {
            mutableListOf(tName)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.idCompany <= 0) throw Exception("No se ha establecido la Empresa...")
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