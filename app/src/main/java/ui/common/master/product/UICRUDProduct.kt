package com.vsg.agendaandpublication.ui.common.itemProduct.product

import android.widget.*
import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemOperation.setting.SectionDefaultViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section.Section
import com.vsg.agendaandpublication.common.model.itemProduct.category.Category
import com.vsg.agendaandpublication.common.model.itemProduct.company.Company
import com.vsg.agendaandpublication.common.model.itemProduct.product.Product
import com.vsg.agendaandpublication.common.model.itemProduct.product.ProductDao
import com.vsg.agendaandpublication.common.model.itemProduct.product.ProductViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.unit.Unit
import com.vsg.agendaandpublication.ui.activities.itemOperation.helper.ChooseSectionByWarehouse
import com.vsg.utilities.common.operation.DBOperation
import com.vsg.utilities.helper.bitmap.HelperBitmap.Companion.toArray
import com.vsg.utilities.helper.bitmap.HelperBitmap.Companion.toBitmap
import com.vsg.utilities.helper.string.HelperString.Static.toLineSpanned
import com.vsg.utilities.ui.crud.UICustomCRUDViewModelRelation
import com.vsg.utilities.ui.crud.helper.ChoosePicture
import com.vsg.utilities.ui.util.CurrentBaseActivity
import com.vsg.utilities.ui.widget.spinner.CustomSpinner
import com.vsg.utilities.ui.widget.text.CustomInputText

@ExperimentalStdlibApi
class UICRUDProduct<TActivity>(
    activity: TActivity,
    operation: DBOperation,
    viewModel: ProductViewModel,
    val company: Company
) :
    UICustomCRUDViewModelRelation<TActivity, ProductViewModel, ProductDao, Product, Company>(
        activity,
        operation,
        R.layout.dialog_product,
        viewModel,
        idTextParent = R.id.DialogProductRelation,
        parent = company
    )
        where TActivity : CurrentBaseActivity<ProductViewModel, ProductDao, Product> {

    //region widget
    private lateinit var tCategory: CustomSpinner
    private lateinit var tName: CustomInputText
    private lateinit var tProviderCode: CustomInputText
    private lateinit var tUnit: CustomSpinner
    private lateinit var tHasStock: CheckBox
    private lateinit var choosePicture: ChoosePicture
    private lateinit var chooseSection: ChooseSectionByWarehouse
    private var section: Section? = null
    private var category: Category? = null
    private var unit: Unit? = null
    //endregion

    override fun aGetTextParent(): String = parent.name

    init {
        onEventSetInit = {
            choosePicture = ChoosePicture(it, activity)
            chooseSection = ChooseSectionByWarehouse(it, viewModel.context, parent).apply {
                onEventSectionSelected = { sec -> this@UICRUDProduct.section = sec }
            }
            this.tCategory = it.findViewById<CustomSpinner>(R.id.DialogProductCategory).apply {
                setCustomAdapter(
                    viewModel.viewModelGetCategories(),
                    { c -> category = c },
                )
            }
            this.tName = it.findViewById(R.id.DialogProductName)
            this.tProviderCode = it.findViewById(R.id.DialogProductProviderCode)

            this.tUnit = it.findViewById<CustomSpinner>(R.id.DialogProductUnit).apply {
                setCustomAdapter(viewModel.viewModelGetUnits(), { c -> unit = c })
            }
            this.tHasStock = it.findViewById(R.id.DialogProductHasStock)
            setSectionDefault()
        }
        onEventGetNewOrUpdateEntity = {
            val data = it ?: Product()
            data.apply {
                this.name = tName.text
                this.picture = choosePicture.getArray()
                this.stockMin = 0.0
                this.stockMax = 0.0
                this.providerCode = tProviderCode.text
                this.hasStock = tHasStock.isChecked

                this.idCompany = this@UICRUDProduct.company.id
                this.idCategory = this@UICRUDProduct.category?.id ?: 0
                this.idSection = this@UICRUDProduct.section?.id ?: 0
                this.idUnit = this@UICRUDProduct.unit?.id ?: 0
            }
            data
        }
        onEventSetItem = {
            tCategory.setItem<Category>(it.idCategory)
            choosePicture.setBitmap(it.picture)
            tName.text = it.name
            tProviderCode.text = it.providerCode
            tUnit.setItem<Unit>(it.idUnit)
            tHasStock.isChecked = it.hasStock
            chooseSection.setSection(it.idSection)
        }
        onEventSetItemsForClean = {
            setSectionDefault()
            mutableListOf(tName, choosePicture.tPicture, tProviderCode, tHasStock)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.idCompany <= 0) throw Exception("No se ha establecido la Empresa...")
                if (item.idCategory <= 0) throw Exception("No se ha establecido la Categoría...")
                if (item.idUnit <= 0) throw Exception("No se ha establecido la Unidad de medida...")
                if (item.idSection <= 0) throw Exception("No se ha establecido una localización...")
                if (item.name.isEmpty()) throw Exception("El nombre del Producto no puede ser nulo...")
                if (item.picture == null || item.picture!!.isEmpty()) {
                    item.picture = activity.toBitmap(R.drawable.pic_product).toArray()
                }
                result = true
            } catch (e: Exception) {
                message(e.message ?: "Error desconocido...")
            }
            result
        }
        onEventGetPopUpDataParameter = { p, item ->
            p?.factorHeight = 0.45
            if (item != null && p != null) {
                p.icon = item.getDrawableShow()
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

    private fun setSectionDefault() {
        if (viewModel == null) return
        chooseSection.setSection(
            SectionDefaultViewModel(viewModel.context).viewModelGetDefaultSectionByCompany(
                parent.id
            )
        )
    }
}