package com.vsg.agendaandpublication.ui.activities.itemProducto.main

import android.widget.ScrollView
import androidx.lifecycle.ViewModelProvider
import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemProduct.price.Price
import com.vsg.agendaandpublication.common.model.itemProduct.price.PriceViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.product.Product
import com.vsg.agendaandpublication.common.model.itemProduct.product.ProductViewModel
import com.vsg.ot.ui.activities.master.CompanyActivity
import com.vsg.agendaandpublication.ui.activities.itemProducto.ProductActivity
import com.vsg.agendaandpublication.ui.activities.itemProducto.UnitActivity
import com.vsg.utilities.common.util.viewModel.util.FilterMemberInclude
import com.vsg.utilities.ui.util.BaseActivity
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
class MainItemProducto : BaseActivity(R.layout.main_item_producto) {
    init {
        onEventMakeActivityForSelector = {
            val view: ScrollView = it.apply {
                add(
                    R.drawable.pic_company,
                    getString(R.string.ActivityItemProductCompanyText),
                    dataClass = CompanyActivity::class.java
                )
                add(
                    R.drawable.pic_category,
                    getString(R.string.ActivityItemProductCategoryText),
                    dataClass = CategoryActivity::class.java
                )
                add(
                    R.drawable.pic_unit,
                    getString(R.string.ActivityItemProductUnitText),
                    dataClass = UnitActivity::class.java
                )
                add(
                    R.drawable.pic_product,
                    getString(R.string.ActivityItemProductProductoText),
                    dataClass = ProductActivity::class.java
                )
                add(
                    R.drawable.pic_back,
                    "test",
                    action = {

                        //region product:
                        val vm =
                            run { ViewModelProvider(this@MainItemProducto).get(ProductViewModel::class.java) }
                        val product: Product? =
                            vm.viewModelView(
                                1,
                                include = arrayOf(
                                    FilterMemberInclude(entity = typeOf<Unit>()),
                                    FilterMemberInclude(list = typeOf<List<Price>>())
                                )
                            )
                        //endregion

                        //region price:
                        val vmPrice =
                            run { ViewModelProvider(this@MainItemProducto).get(PriceViewModel::class.java) }
                        val price: Price? =
                            vmPrice.viewModelView(
                                1,
                                include = arrayOf(FilterMemberInclude(typeOf<Product>()).apply {
                                    addEntity(typeOf<Unit>())
                                    addList(typeOf<List<Price>>())
                                })
                            )

                        //endregion

                        val r = 3
                    }
                )
            }.makeLayout()
            view
        }
    }


    override fun onExecuteCreate() {
        setActivityAsSelector()
    }
}