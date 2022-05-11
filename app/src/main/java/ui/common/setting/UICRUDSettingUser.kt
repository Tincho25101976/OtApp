package com.vsg.ot.ui.common.setting

import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.helper.exception.HelperException.Companion.throwException
import com.vsg.helper.helper.string.HelperString.Static.isMailAddress
import com.vsg.helper.ui.crud.UICustomCRUDViewModel
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.spinner.CustomSpinner
import com.vsg.helper.ui.widget.text.CustomInputText
import com.vsg.ot.R
import com.vsg.ot.common.model.setting.user.SettingUser
import com.vsg.ot.common.model.setting.user.SettingUserDao
import com.vsg.ot.common.model.setting.user.SettingUserViewModel
import common.model.master.item.type.TypePlant

@ExperimentalStdlibApi
class UICRUDSettingUser<TActivity>(activity: TActivity, operation: DBOperation) :
    UICustomCRUDViewModel<TActivity, SettingUserViewModel, SettingUserDao, SettingUser>(
        activity,
        operation,
        R.layout.dialog_setting_user
    )
        where TActivity : CurrentBaseActivity<SettingUserViewModel, SettingUserDao, SettingUser> {

    //region widget
    private lateinit var tName: CustomInputText
    private lateinit var tMail: CustomInputText
    private lateinit var tUserId: CustomInputText
    private lateinit var tPlant: CustomSpinner
    //endregion

    override fun aGetEntityAllowDefault(): Boolean = isEntityAllowDefault<SettingUser>()

    init {
        onEventSetInit = {
            this.tName = it.findViewById(R.id.DialogSettingUserName)
            this.tUserId = it.findViewById(R.id.DialogSettingUserId)
            this.tMail = it.findViewById(R.id.DialogSettingUserMail)
            this.tPlant = it.findViewById<CustomSpinner>(R.id.DialogSettingUserPlant).apply {
                setCustomAdapterEnum(TypePlant::class.java)
            }
        }
        onEventGetNewOrUpdateEntity = {
            val data = it ?: SettingUser()
            data.apply {
                this.name = tName.text
                this.valueCode = tUserId.text
                this.mail = tMail.text
                this.planta = tPlant.getItemEnumOrDefault() ?: TypePlant.UNDEFINED
            }
            data
        }
        onEventSetItem = {
            tName.text = it.name
            tUserId.text = it.valueCode
            tMail.text = it.mail
            tPlant.setItemEnum(it.planta)
        }
        onEventSetItemsForClean = {
            mutableListOf(tName, tUserId, tMail)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.name.isEmpty()) "El nombre del usuario no puede ser nulo".throwException()
                if (item.mail.isEmpty()) "El correo del usuario no puede ser nulo".throwException()
                if (!item.mail.isMailAddress()) "El formato del correo es incorrecto".throwException()
                if (item.valueCode.isEmpty()) "El ID del usuario no puede ser nulo".throwException()
                if (item.planta == TypePlant.UNDEFINED) "No se ha seleccionado una Planta".throwException()
                result = true
            } catch (e: Exception) {
                message(e.message ?: "Error desconocido...")
            }
            result
        }
        onEventGetPopUpDataParameter = { p, item ->
            p?.factorHeight = 0.45
            if (item != null) {
                p?.icon = item.getDrawableShow().drawable
                p?.bitmap = item.getPictureShow()
                p?.toHtml = item.reference()
            }
            p
        }
    }
}