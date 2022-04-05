package com.vsg.agendaandpublication.ui.common.itemProduct.unit

import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemProduct.unit.Unit
import com.vsg.agendaandpublication.common.model.itemProduct.unit.UnitDao
import com.vsg.agendaandpublication.common.model.itemProduct.unit.UnitViewModel
import com.vsg.agendaandpublication.ui.common.itemProduct.unit.util.UnitUtilAutoComplete
import com.vsg.utilities.common.operation.DBOperation
import com.vsg.utilities.helper.HelperUI.Static.setCustomAdapter
import com.vsg.utilities.helper.HelperUI.Static.toEditable
import com.vsg.utilities.helper.HelperUI.Static.toText
import com.vsg.utilities.ui.crud.UICustomCRUDViewModel
import com.vsg.utilities.ui.util.CurrentBaseActivity
import com.vsg.utilities.ui.widget.text.CustomInputText

@ExperimentalStdlibApi
class UICRUDUnit<TActivity>(activity: TActivity, operation: DBOperation) :
    UICustomCRUDViewModel<TActivity, UnitViewModel, UnitDao, Unit>(
        activity,
        operation,
        R.layout.dialog_unit
    )
        where TActivity : CurrentBaseActivity<UnitViewModel, UnitDao, Unit> {

    //region widget
    private lateinit var tName: AutoCompleteTextView
    private lateinit var tSymbol: CustomInputText
    private lateinit var tDecimals: CustomInputText
    private lateinit var tIsDecimal: CheckBox
    private var listUnit: List<Unit> = UnitUtilAutoComplete().list
    //endregion

    init {
        onEventSetInit = {
            this.tName = it.findViewById<AutoCompleteTextView>(R.id.DialogUnitName).apply {
                setCustomAdapter(
                    context = activity,
                    adapter = listUnit.map { s -> s.name },
                    ignoreCase = true,
                    callbackOnItemClick = { _, t -> search(t) },
                    callbackOnKeyPressEnter = { _, t -> search(t) })
            }
            this.tSymbol = it.findViewById(R.id.DialogUnitSymbol)
            this.tDecimals = it.findViewById(R.id.DialogUnitDecimals)
            this.tIsDecimal = it.findViewById<CheckBox>(R.id.DialogUnitIsInteger).apply {
                when (isChecked) {
                    true -> tDecimals.int = 0
                }
                tDecimals.isEnabled = !isChecked
            }
        }
        onEventGetNewOrUpdateEntity = {
            val data = it ?: Unit()
            data.apply {
                this.name = tName.toText()
                this.symbol = tSymbol.text
                this.isDecimal = tIsDecimal.isChecked
                this.decimals = tDecimals.int ?: 0
            }
            data
        }
        onEventSetItem = {
            tName.text = it.name.toEditable()
            tSymbol.text = it.symbol
            tIsDecimal.isChecked = it.isDecimal
            tDecimals.int = it.decimals
        }
        onEventSetItemsForClean = {
            mutableListOf(tName, tSymbol)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.name.isEmpty()) throw Exception("El nombre de la Unidad no puede ser nulo...")
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
            }
            p
        }
    }

    override fun oIsEntityOnlyOneDefault() = isEntityOnlyOneDefaultTrue<Unit>()
    private fun search(find: String) {
        if (!listUnit.any()) return
        val f = listUnit.firstOrNull { it.name == find } ?: return
        tSymbol.text = f.symbol
        tIsDecimal.isChecked = f.isDecimal
        tDecimals.int = f.decimals
    }
}