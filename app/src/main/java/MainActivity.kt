package com.vsg.ot

import android.widget.ScrollView
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.ot.ui.activities.master.main.MainMaster
import com.vsg.ot.ui.activities.securityDialog.main.MainSecurityDialog
import com.vsg.ot.ui.activities.setting.main.MainSetting

//TODO Path app:  /data/data/com.vsg.ot

@ExperimentalStdlibApi
class MainActivity : BaseActivity(R.layout.activity_main) {

    init {
        onEventMakeActivityForSelector = {
            val view: ScrollView = it.apply {
                add(
                    R.drawable.pic_xact_main,
                    getString(R.string.ActivityMainCommandXact),
                    dataClass = MainSecurityDialog::class.java
                )
                add(
                    R.drawable.pic_product,
                    getString(R.string.ActivityMainCommandMaster),
//                    dataClass = MainMaster::class.java
                )
//                add(
//                    R.drawable.pic_setting,
//                    getString(R.string.ActivityMainCommandSetting),
//                    dataClass = MainSetting::class.java
//                )
//                add(
//                    R.drawable.pic_operation,
//                    getString(R.string.ActivityMainCommandOperation),
//                    dataClass = MainItemOperation::class.java
//                )
//                add(
//                    R.drawable.pic_add,
//                    getString(R.string.ActivityMainCommandAutoFillData),
//                    action = {
//                        AutoFillDataContacts(this@MainActivity).apply {
//                            makeContacts(Random.nextInt(1, 10))
//                        }
//                        AutoFillDataProducts(this@MainActivity).apply {
//                            makeProductsRelation(1, 5)
//                        }
//                        AutoFillOperation(this@MainActivity).apply {
//                            makeOperationRelation(people = 5, warehouses = 5, sections = 3)
//                        }
//                        AutoFillDataProducts(this@MainActivity).apply {
//                            makeProducts(10)
//                            makeProductsItems()
//                        }
//                    }
//                )
//                add(
//                    R.drawable.pic_batch_add,
//                    getString(R.string.ActivityMainCommandAutoFillDataBatch),
//                    dataClass = MainItemOperationAddBatch::class.java
//                )
//                add(
//                    R.drawable.pic_picture_choose_grayscale,
//                    getString(R.string.text_suspenso),
//                    action = {
//                        UICustomDialogViewer(this@MainActivity).apply {
//                            make(
//                                UICustomDialogViewerParameter(
//                                    BitmapFactory.decodeResource(
//                                        resources,
//                                        R.drawable.pic_sample
//                                    )
//                                )
//                            )
//                        }
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