package com.vsg.agendaandpublication.ui.common.itemOperation.warehouse

import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.TypeSection
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section.Section
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section.SectionDao
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section.SectionViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse.Warehouse
import com.vsg.utilities.common.operation.DBOperation
import com.vsg.utilities.helper.date.HelperDate.Companion.now
import com.vsg.utilities.ui.crud.UICustomCRUDViewModelRelation
import com.vsg.utilities.ui.util.CurrentBaseActivity
import com.vsg.utilities.ui.widget.spinner.CustomSpinner
import com.vsg.utilities.ui.widget.text.CustomInputText

@ExperimentalStdlibApi
class UICRUDSection<TActivity>(
    activity: TActivity,
    operation: DBOperation,
    warehouse: Warehouse,
) :
    UICustomCRUDViewModelRelation<TActivity, SectionViewModel, SectionDao, Section, Warehouse>(
        activity,
        operation,
        R.layout.dialog_section,
        idTextParent = R.id.DialogSectionRelation,
        parent = warehouse
    )
        where TActivity : CurrentBaseActivity<SectionViewModel, SectionDao, Section> {

    //region widget
    private lateinit var tName: CustomInputText
    private lateinit var tType: CustomSpinner
    //endregion

    override fun aGetTextParent(): String = parent.name
    override fun oIsEntityOnlyOneDefault() = isEntityOnlyOneDefaultTrue<Section>()

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
            var data: Section? = null
            if (type != null) {
                data = it ?: Section()
                data.apply {
                    this.createDate = now()
                    this.name = tName.text
                    this.type = type
                    this.idWarehouse = parent.id
                    this.idCompany = parent.idCompany
                }
            }
            data
        }
        onEventSetItem = {
            tName.text = it.name
            tType.setItemEnum(it.type)
        }
        onEventSetItemsForClean = {
            mutableListOf(tName)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.name.isEmpty()) throw Exception("El nombre de la localización no puede ser nulo...")
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
                p?.icon = item.getDrawableShow()
                p?.bitmap = item.getPictureShow()
            }
            p
        }
    }
}