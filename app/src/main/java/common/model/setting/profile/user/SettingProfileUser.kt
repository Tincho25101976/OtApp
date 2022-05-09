package com.vsg.ot.common.model.setting.profile.user

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import com.vsg.ot.common.model.setting.profile.SettingProfile
import com.vsg.ot.common.model.setting.user.SettingUser
import common.model.init.entity.EntityOt

@Entity(tableName = SettingProfileUser.ENTITY_NAME)
class SettingProfileUser : EntityOt<SettingProfileUser>() {

    //region properties
    @EntityForeignKeyID(10)
    @ColumnInfo(index = true)
    var idProfile: Int = 0

    @EntityForeignKeyID(10)
    @Ignore
    var profile: SettingProfile? = null

    @EntityForeignKeyID(20)
    @ColumnInfo(index = true)
    var idUser: Int = 0

    @EntityForeignKeyID(20)
    @Ignore
    var user: SettingUser? = null
    //endregion

    //region methods
    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_setting_profile_user

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(description)

    override fun aEquals(other: Any?): Boolean {
        if (other !is SettingProfileUser) return false
        return valueCode == other.valueCode
                && description == other.description
                && createDate.time == other.createDate.time
                && idProfile == other.idProfile
                && idUser == other.idUser
    }

    companion object {
        const val ENTITY_NAME = "masterSettingProfileUser"
    }
}