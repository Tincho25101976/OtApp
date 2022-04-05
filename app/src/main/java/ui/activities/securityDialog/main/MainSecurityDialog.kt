package com.vsg.ot.ui.activities.securityDialog.main

import android.widget.ScrollView
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.ot.R
import ui.activities.securityDialog.xact.XactActivity

@ExperimentalStdlibApi
class MainSecurityDialog : BaseActivity(R.layout.main_security_dialog) {
    init {
        onEventMakeActivityForSelector = {
            val view: ScrollView = it.apply {
                add(
                    R.drawable.pic_xact,
                    getString(R.string.ActivityItemProductCategoryText),
                    dataClass = XactActivity::class.java
                )
//                add(
//                    R.drawable.pic_category,
//                    getString(R.string.ActivityItemProductCategoryText),
//                    dataClass = CategoryActivity::class.java
//                )
//                add(
//                    R.drawable.pic_unit,
//                    getString(R.string.ActivityItemProductUnitText),
//                    dataClass = UnitActivity::class.java
//                )
//                add(
//                    R.drawable.pic_product,
//                    getString(R.string.ActivityItemProductProductoText),
//                    dataClass = ProductActivity::class.java
//                )
//                add(
//                    R.drawable.pic_back,
//                    "test",
//                    action = {
//
//                        //region product:
//                        val vm =
//                            run { ViewModelProvider(this@MainSecurityDialog).get(ProductViewModel::class.java) }
//                        val product: MasterItem? =
//                            vm.viewModelView(
//                                1,
//                                include = arrayOf(
//                                    FilterMemberInclude(entity = kotlin.reflect.typeOf<Unit>()),
//                                    FilterMemberInclude(list = kotlin.reflect.typeOf<List<Price>>())
//                                )
//                            )
//                        //endregion
//
//                        //region price:
//                        val vmPrice =
//                            run { ViewModelProvider(this@MainItemProducto).get(PriceViewModel::class.java) }
//                        val price: Price? =
//                            vmPrice.viewModelView(
//                                1,
//                                include = arrayOf(FilterMemberInclude(kotlin.reflect.typeOf<Product>()).apply {
//                                    addEntity(kotlin.reflect.typeOf<Unit>())
//                                    addList(kotlin.reflect.typeOf<List<Price>>())
//                                })
//                            )
//
//                        //endregion
//
//                        val r = 3
//                    }
//                )
            }.makeLayout()
            view
        }
    }


    override fun onExecuteCreate() {
        setActivityAsSelector()
    }
}