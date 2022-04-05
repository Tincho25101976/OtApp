package com.vsg.ot.ui.autoFill

import androidx.annotation.IntRange
import com.vsg.helper.ui.util.BaseActivity
import common.model.master.company.MasterCompany

import java.util.*
import kotlin.random.Random

@ExperimentalStdlibApi
class AutoFillDataProducts(app: BaseActivity) : AutoFillDataBase(app) {
    private var utilPalabras: Map<Int, String> = mutableMapOf()

    fun makeProductsRelation(
        @IntRange(from = 1, to = 10) companies: Int,
        @IntRange(from = 1, to = 150) categories: Int
    ) {
        makeViewModel(CompanyViewModel::class.java).apply {
            val temp = getRandomCompany(companies)
            temp.forEach { this.viewModelInsert(it) }
            getMessage(temp.count(), "Empresa", "Empresas")
        }
        makeViewModel(CategoryViewModel::class.java).apply {
            val temp = getRandomCategory(categories)
            temp.forEach { this.viewModelInsert(it) }
            getMessage(temp.count(), "Categoría", "Categorías")
        }
        makeViewModel(UnitViewModel::class.java).apply {
            val temp = getUnit()
            temp.forEach { this.viewModelInsert(it) }
            getMessage(temp.count(), "Unidad", "Unidades")
        }
    }

    fun makeProducts(@IntRange(from = 2, to = 250) count: Int) {
        utilPalabras = getMakeListFromFile("words")
        var companies: List<MasterCompany>
        var categories: List<Category>
        var units: List<Unit>
        var sections: List<Section>
        makeViewModel(CompanyViewModel::class.java).apply {
            companies = this.viewModelViewAllSimpleList()
        }
        makeViewModel(CategoryViewModel::class.java).apply {
            categories = this.viewModelViewAllSimpleList()
        }
        makeViewModel(UnitViewModel::class.java).apply {
            units = this.viewModelViewAllSimpleList()
        }
        makeViewModel(SectionViewModel::class.java).apply {
            sections = this.viewModelViewAllSimpleList()
        }
        makeViewModel(ProductViewModel::class.java).apply {
            val temp = getRandomProducts(companies, categories, units, sections, count) ?: listOf()
            temp.forEach {
                this.viewModelInsert(it)
            }
            getMessage(temp.count(), "Producto", "Productos")
        }
    }

    fun makeProductsItems() {
        var companies: List<Company>
        makeViewModel(CompanyViewModel::class.java).apply {
            companies = this.viewModelViewAllSimpleList()
        }
        var countPrices = 0
        var countPictures = 0
        if (companies.any()) {
            try {
                makeViewModel(ProductViewModel::class.java).apply {
                    companies.forEach { c ->
                        val tempProducts = this.viewModelViewAllSimpleList(c.id)
                        tempProducts.forEach { p ->
                            val tempPrices = getPricesForProduct(p)
                            if (tempPrices.any()) {
                                makeViewModel(PriceViewModel::class.java).apply {
                                    this.viewModelInsert(tempPrices)
                                    countPrices += tempPrices.count()
                                }
                            }

                            val tempPictures = getPicturesForProduct(p)
                            if (tempPictures.any()) {
                                makeViewModel(PictureViewModel::class.java).apply {
                                    this.viewModelInsert(tempPictures)
                                    countPictures += tempPictures.count()
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                getMessage(message = e.message)
            }
        }
        getMessage(countPrices, "Precio", "Precios")
        getMessage(countPictures, "Imagen", "Imágenes")
    }

    //region producto
    private fun getRandomCompany(count: Int): List<Company> {
        val list: MutableList<Company> = mutableListOf()
        repeat(count) {
            val value = it + 1
            val text = "Empresa ${value.toPadStart()}"
            list.add(Company().apply {
                name = text
                picture = makePicture(text)
            })
        }
        return list
    }

    private fun getRandomCategory(count: Int): List<Category> {
        val list: MutableList<Category> = mutableListOf()
        repeat(count) {
            val value = it + 1
            val text = "Categoría ${value.toPadStart()}"
            list.add(Category().apply {
                name = text
                picture = makePicture(text)
            })
        }
        return list
    }

    private fun getUnit(): List<Unit> = UnitUtilAutoComplete().list

    private fun getRandomProducts(
        company: List<Company>,
        category: List<Category>,
        unit: List<Unit>,
        section: List<Section>,
        count: Int = 15
    ): List<Product>? {
        val mapCompany = getMap(company) ?: return null
        val mapCategory = getMap(category) ?: return null
        val mapUnit = getMap(unit) ?: return null
        val data: MutableList<Product> = mutableListOf()

        var item = 0
        val tempCompany = getRandomEntity(mapCompany)
        val tempCategory = getRandomEntity(mapCategory)
        val tempUnit = getRandomEntity(mapUnit)
        val tempProduct = Random.nextInt(1, count)


        val total: Int = tempCompany.count() * tempCategory.count() * tempUnit.count() * tempProduct

        if (section.any()) {
            tempCompany.filter { com -> section.any { sec -> sec.idCompany == com.id } }
                .forEach { com ->
                    tempCategory.forEach { cat ->
                        tempUnit.forEach { un ->
                            repeat(tempProduct) {
                                data.add(
                                    getProduct(
                                        com,
                                        cat,
                                        un,
                                        section.filter { sec -> sec.idCompany == com.id }
                                            .toRandom()!!,
                                        ++item))
                                onEventProgress?.invoke(total, item)
                            }
                        }
                    }
                }
        }
        return data
    }

    private fun getProduct(
        company: Company,
        category: Category,
        unit: Unit,
        section: Section,
        id: Int
    ): Product {
        val data = Product().apply {
            idCompany = company.id
            idCategory = category.id
            idSection = section.id
            idUnit = unit.id
            name = "Producto ${id.toPadStart()}"
            stockMin = Random.nextDouble(1.0, 10.0)
            stockMax = Random.nextDouble(stockMin + 10.0, 100.0)
            hasStock = true
            providerCode = getRandomCode()
            isEnabled = true
            isDefault = false
            description = getRandomDescription()
            picture = getPictureArray()
            shellLife = Random.nextInt(2, 12) * 30
            shellLifeAlert = Random.nextInt(1, 2) * 30
        }
        return data
    }

    private fun getPricesForProduct(product: Product): List<Price> {
        val dates: MutableList<Date> = mutableListOf()
        repeat(1.toRandom(10)) { dates.add(getRandomDate(2020, 2020)) }
        if (!dates.any()) return emptyList()
        val valuePrice: Double = 1.toRandom(1000).toDouble()
        return dates.sortedBy { it.time }.map {
            Price().apply {
                value = valuePrice.toRandom((valuePrice + 10))
                createDate = it
                idProduct = product.id
                isDefault = false
                isEnabled = true
                description = ""
            }
        }
    }

    private fun getPicturesForProduct(product: Product): List<Picture> {
        val dates: MutableList<Date> = mutableListOf()
        repeat(1.toRandom(5)) { dates.add(getRandomDate(2020, 2020)) }
        if (!dates.any()) return emptyList()
        return dates.sortedBy { it.time }.map {
            Picture().apply {
                picture = makePicture(product.name, SIZE_HEIGHT, SIZE_WIDTH)
                createDate = it
                idProduct = product.id
                isDefault = false
                isEnabled = true
                description = ""
            }
        }
    }

    //endregion

    companion object {
        const val SIZE_HEIGHT: Int = 1204
        const val SIZE_WIDTH: Int = 768
    }
}