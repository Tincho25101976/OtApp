package com.vsg.ot.ui.common.setting

import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.helper.exception.HelperException.Companion.throwException
import com.vsg.helper.ui.crud.UICustomCRUDViewModel
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.ot.R
import com.vsg.ot.common.model.setting.menu.SettingMenu
import com.vsg.ot.common.model.setting.menu.SettingMenuDao
import com.vsg.ot.common.model.setting.menu.SettingMenuViewModel

@ExperimentalStdlibApi
class UICRUDSettingMenu<TActivity>(activity: TActivity, operation: DBOperation) :
    UICustomCRUDViewModel<TActivity, SettingMenuViewModel, SettingMenuDao, SettingMenu>(
        activity,
        operation,
        R.layout.dialog_setting_user
    )
        where TActivity : CurrentBaseActivity<SettingMenuViewModel, SettingMenuDao, SettingMenu> {

    override fun aGetEntityAllowDefault(): Boolean = isEntityAllowDefault<SettingMenu>()

    init {
        onEventSetInit = {

        }
//        onEventGetNewOrUpdateEntity = {
//
//        }
        onEventSetItem = {

        }
//        onEventSetItemsForClean = {
//
//        }
        onEventValidate = { item, _ ->
            var result = true
            try {
                if (item.description.isEmpty()) "El nombre del menú no puede ser nulo".throwException()
                result = true
            } catch (e: Exception) {
                message(e.message ?: "Error desconocido...")
            }
            result
        }
        onEventGetPopUpDataParameter = { p, item ->
            p?.factorHeight = 0.35
            if (item != null) {
                p?.icon = item.getDrawableShow().drawable
                p?.bitmap = item.getPictureShow()
                p?.toHtml = item.reference()
            }
            p
        }
    }
}