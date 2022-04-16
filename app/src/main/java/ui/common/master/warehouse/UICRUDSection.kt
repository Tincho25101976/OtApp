package com.vsg.ot.ui.common.master.warehouse

import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.helper.date.HelperDate.Companion.now
import com.vsg.helper.ui.crud.UICustomCRUDViewModelRelation
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.spinner.CustomSpinner
import com.vsg.helper.ui.widget.text.CustomInputText
import com.vsg.ot.R
import common.model.master.section.MasterSection
import common.model.master.section.MasterSectionDao
import common.model.master.section.MasterSectionViewModel
import common.model.master.section.type.TypeSection
import common.model.master.warehouse.MasterWarehouse

@ExperimentalStdlibApi
class UICRUDSection<TActivity>(
    activity: TActivity,
    operation: DBOperation,
    warehouse: MasterWarehouse,
) :
    UICustomCRUDViewModelRelation<TActivity, MasterSectionViewModel, MasterSectionDao, MasterSection, MasterWarehouse>(
        activity,
        operation,
        R.layout.dialog_master_section,
        idTextParent = R.id.DialogSectionRelation,
        parent = warehouse
    )
        where TActivity : CurrentBaseActivity<MasterSectionViewModel, MasterSectionDao, MasterSection> {

    //region widget
    private lateinit var tName: CustomInputText
    private lateinit var tType: CustomSpinner
    //endregion

    override fun aGetTextParent(): String = parent.description
    override fun oIsEntityOnlyOneDefault() = isEntityOnlyOneDefaultTrue<MasterSection>()

    init {
        onEventSetInit = {
            this.tName = it.findViewById(R.id.DialogSectionName)
            this.tType =
                it.findViewById<CustomSpinner>(R.id.DialogSectionType).apply {
                    setCustomAdapterEnum(TypeSection::class.java)
                }


        }
        onEventGetNewOrUpdateEntity = {
            val type: TypeSection? = tType.getItemEnumOrDefault()
            var data: MasterSection? = null
            if (type != null) {
                data = it ?: MasterSection()
                data.apply {
                    this.createDate = now()
                    this.description = tName.text
                    this.type = type
                    this.idWarehouse = parent.id
                    this.idCompany = parent.idCompany
                }
            }
            data
        }
        onEventSetItem = {
            tName.text = it.description
            tType.setItemEnum(it.type)
        }
        onEventSetItemsForClean = {
            mutableListOf(tName)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.description.isEmpty()) throw Exception("El nombre de la localización no puede ser nulo...")
                if (item.type == TypeSection.UNDEFINED) throw Exception("El tipo de la divisa no puede ser ${TypeSection.UNDEFINED.title}...")
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