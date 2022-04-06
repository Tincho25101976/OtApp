package com.vsg.ot.ui.autoFill

import androidx.annotation.IntRange
import com.vsg.helper.helper.HelperEnum.Companion.toRandomEnum
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.helper.util.helper.HelperNumeric.Companion.toPadStart
import com.vsg.helper.util.unit.type.TypeUnit
import common.model.master.company.MasterCompany
import common.model.master.company.MasterCompanyViewModel
import common.model.master.item.MasterItem
import common.model.master.item.MasterItemViewModel
import common.model.master.item.type.TypePlant
import common.model.master.item.type.TypeProduct
import common.model.master.section.MasterSection
import common.model.master.section.MasterSectionViewModel
import kotlin.random.Random

@ExperimentalStdlibApi
class AutoFillDataMasterItems(app: BaseActivity) : AutoFillDataBase(app) {
    private var utilPalabras: Map<Int, String> = mutableMapOf()

    fun makeMasterItemsRelation(
        @IntRange(from = 1, to = 10) companies: Int
    ) {
        makeViewModel(MasterCompanyViewModel::class.java).apply {
            val temp = getRandomCompany(companies)
            temp.forEach { this.viewModelInsert(it) }
            getMessage(temp.count(), "Empresa", "Empresas")
        }
    }

    fun makeMasterItems(@IntRange(from = 2, to = 250) count: Int) {
        utilPalabras = getMakeListFromFile("words")
        var companies: List<MasterCompany>
        var units: List<Unit>
        var sections: List<MasterSection>
        makeViewModel(MasterCompanyViewModel::class.java).apply {
            companies = this.viewModelViewAllSimpleList()
        }
        makeViewModel(MasterSectionViewModel::class.java).apply {
            sections = this.viewModelViewAllSimpleList()
        }
        makeViewModel(MasterItemViewModel::class.java).apply {
            val temp = getRandomMasterItems(companies, count) ?: listOf()
            temp.forEach {
                this.viewModelInsert(it)
            }
            getMessage(temp.count(), "MasterItemo", "MasterItemos")
        }
    }

    fun makeMasterItems() {
        var companies: List<MasterCompany>
        makeViewModel(MasterCompanyViewModel::class.java).apply {
            companies = this.viewModelViewAllSimpleList()
        }
        if (companies.any()) {
            try {
                makeViewModel(MasterItemViewModel::class.java).apply {
                    companies.forEach { c ->
                        val tempMasterItems = this.viewModelViewAllSimpleList(c.id)
                        tempMasterItems.forEach { p ->

                        }
                    }
                }
            } catch (e: Exception) {
                getMessage(message = e.message)
            }
        }
    }

    //region ítem
    private fun getRandomCompany(count: Int): List<MasterCompany> {
        val list: MutableList<MasterCompany> = mutableListOf()
        repeat(count) {
            val value = it + 1
            val text = "Company ${value.toPadStart()}"
            list.add(MasterCompany().apply {
                description = text
                picture = makePicture(text)
            })
        }
        return list
    }

    private fun getRandomMasterItems(
        company: List<MasterCompany>,
        count: Int = 15
    ): List<MasterItem>? {
        val mapCompany = getMap(company) ?: return null
        val data: MutableList<MasterItem> = mutableListOf()

        var item = 0
        val tempCompany = getRandomEntity(mapCompany)
        val tempMasterItem = Random.nextInt(1, count)
        val total: Int = tempCompany.count() * tempMasterItem

        tempCompany
            .forEach { com ->
                repeat(tempMasterItem) {
                    data.add(
                        getMasterItem(com, ++item)
                    )
                    onEventProgress?.invoke(total, item)
                }
            }
        return data
    }

    private fun getMasterItem(company: MasterCompany, id: Int): MasterItem {
        val data = MasterItem().apply {
            idCompany = company.id
            description = "MasterItem ${id.toPadStart()}"

            typeUnit = TypeUnit::class.java.toRandomEnum(TypeUnit.KG)
            typePlant = TypePlant::class.java.toRandomEnum(TypePlant.BURZACO_QUIMICOS)
            typeProduct = TypeProduct::class.java.toRandomEnum(TypeProduct.FINISH_PRODUCT)

            isEnabled = true
            isDefault = false
            description = getRandomDescription()
            shellLife = Random.nextInt(2, 12) * 30
            shellLifeAlert = Random.nextInt(1, 2) * 30
        }
        return data
    }
    //endregion

    companion object {
        const val SIZE_HEIGHT: Int = 1204
        const val SIZE_WIDTH: Int = 768
    }
}