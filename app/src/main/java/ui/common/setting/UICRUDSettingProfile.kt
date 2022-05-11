package com.vsg.ot.ui.common.setting

import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.helper.exception.HelperException.Companion.throwException
import com.vsg.helper.ui.crud.UICustomCRUDViewModel
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.ot.R
import com.vsg.ot.common.model.setting.profile.SettingProfile
import com.vsg.ot.common.model.setting.profile.SettingProfileDao
import com.vsg.ot.common.model.setting.profile.SettingProfileViewModel

@ExperimentalStdlibApi
class UICRUDSettingProfile<TActivity>(activity: TActivity, operation: DBOperation) :
    UICustomCRUDViewModel<TActivity, SettingProfileViewModel, SettingProfileDao, SettingProfile>(
        activity,
        operation,
        R.layout.dialog_setting_profile
    )
        where TActivity : CurrentBaseActivity<SettingProfileViewModel, SettingProfileDao, SettingProfile> {

    override fun aGetEntityAllowDefault(): Boolean = isEntityAllowDefault<SettingProfile>()

    init {
//        onEventSetInit = {
//
//        }
//        onEventGetNewOrUpdateEntity = {
//
//        }
//        onEventSetItem = {
//
//        }
//        onEventSetItemsForClean = {
//
//        }
        onEventValidate = { item, _ ->
            var result = true
            try {
                if (item.description.isEmpty()) "El nombre del perfil no puede ser nulo".throwException()
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