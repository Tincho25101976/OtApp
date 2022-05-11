package com.vsg.ot.common.model.setting.user

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.Ignore
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import com.vsg.ot.common.model.setting.profile.SettingProfile
import common.model.init.entity.EntityOt
import common.model.master.item.type.TypePlant

@Entity(tableName = SettingUser.ENTITY_NAME)
class SettingUser : EntityOt<SettingUser>() {

    //region properties
    var name: String = ""
    var planta: TypePlant = TypePlant.UNDEFINED
    var mail: String = ""

    @Ignore
    var profiles: List<SettingProfile> = mutableListOf()
    //endregion

    //region methods
    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_setting_user

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(name)

    override fun aEquals(other: Any?): Boolean {
        if (other !is SettingUser) return false
        return valueCode == other.valueCode
                && description == other.description
                && createDate.time == other.createDate.time
                && name == other.name
                && mail == other.mail
                && planta == other.planta
    }
    //endregion

    companion object {
        const val ENTITY_NAME = "masterSettingUser"
    }
}