package com.vsg.ot.ui.common.master.product

import android.widget.*
import com.vsg.ot.R

import com.vsg.ot.ui.activities.master.helper.ChooseSectionByWarehouse
import com.vsg.helper.ui.crud.UICustomCRUDViewModelRelation
import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toArray
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toBitmap
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.ui.crud.helper.ChoosePicture
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.spinner.CustomSpinner
import com.vsg.helper.ui.widget.text.CustomInputText
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem
import common.model.master.item.MasterItemDao
import common.model.master.item.MasterItemViewModel
import common.model.master.section.MasterSection

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
        R.layout.dialog_product,
        viewModel,
        idTextParent = R.id.DialogProductRelation,
        parent = company
    )
        where TActivity : CurrentBaseActivity<MasterItemViewModel, MasterItemDao, MasterItem> {

    //region widget
    private lateinit var tName: CustomInputText
    private lateinit var tUnit: CustomSpinner
    //endregion

    override fun aGetTextParent(): String = parent.description

    init {
        onEventSetInit = {
            this.tName = it.findViewById(R.id.DialogProductName)
            this.tUnit = it.findViewById<CustomSpinner>(R.id.DialogProductUnit).apply {
                setCustomAdapter(viewModel.viewModelGetUnits(), { c -> unit = c })
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
            tUnit.setItem<Unit>(it.idUnit)
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

                val it = viewModel.viewModelGetViewProductContext(item.id)
                if (it != null) {
                    val sb = StringBuilder()
                    sb.toLineSpanned("Empresa", it.company.name)
                    sb.toLineSpanned("Categoría", it.category.name)
                    sb.toLineSpanned("Unidad", it.unit.name)
                    sb.toLineSpanned("Localización", it.section.name)
                    p.toHtml = item.extendedToHTMLPopUp(sb)
                }
            }
            p
        }
    }
}