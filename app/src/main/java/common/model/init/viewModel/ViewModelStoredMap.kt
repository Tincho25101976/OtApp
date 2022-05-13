package common.model.init.viewModel

import android.app.Application
import com.vsg.helper.common.model.viewModel.IViewModelStoredMap
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleListIdRelation
import com.vsg.helper.common.util.viewModel.IViewModelView
import com.vsg.helper.common.util.viewModel.util.DataMakeViewModelView
import com.vsg.ot.common.model.securityDialog.xact.event.XactEvent
import com.vsg.ot.common.model.securityDialog.xact.event.XactEventViewModel
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecord
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecordViewModel
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSector
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSectorViewModel
import com.vsg.ot.common.model.setting.menu.SettingMenu
import com.vsg.ot.common.model.setting.menu.SettingMenuViewModel
import com.vsg.ot.common.model.setting.profile.SettingProfile
import com.vsg.ot.common.model.setting.profile.SettingProfileViewModel
import com.vsg.ot.common.model.setting.profile.menu.SettingProfileMenu
import com.vsg.ot.common.model.setting.profile.menu.SettingProfileMenuViewModel
import com.vsg.ot.common.model.setting.profile.user.SettingProfileUser
import com.vsg.ot.common.model.setting.profile.user.SettingProfileUserViewModel
import com.vsg.ot.common.model.setting.user.SettingUser
import com.vsg.ot.common.model.setting.user.SettingUserDao
import com.vsg.ot.common.model.setting.user.SettingUserViewModel
import common.model.master.batch.MasterBatch
import common.model.master.batch.MasterBatchViewModel
import common.model.master.company.MasterCompany
import common.model.master.company.MasterCompanyViewModel
import common.model.master.item.MasterItem
import common.model.master.item.MasterItemViewModel
import common.model.master.section.MasterSection
import common.model.master.section.MasterSectionViewModel
import common.model.master.warehouse.MasterWarehouse
import common.model.master.warehouse.MasterWarehouseViewModel
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
class ViewModelStoredMap : IViewModelStoredMap {

    private fun storedInstanceOfViewModelView(context: Application): MutableList<DataMakeViewModelView<*, *>> {
        val data: MutableList<DataMakeViewModelView<*, *>> = mutableListOf()

        // Master:
        data.add(DataMakeViewModelView(typeOf<MasterItem>(), MasterItemViewModel(context)))
        data.add(DataMakeViewModelView(typeOf<MasterCompany>(), MasterCompanyViewModel(context)))
        data.add(DataMakeViewModelView(typeOf<MasterBatch>(), MasterBatchViewModel(context)))
        data.add(
            DataMakeViewModelView(
                typeOf<MasterWarehouse>(),
                MasterWarehouseViewModel(context)
            )
        )
        data.add(DataMakeViewModelView(typeOf<MasterSection>(), MasterSectionViewModel(context)))

        // Xact (Records)
        data.add(DataMakeViewModelView(typeOf<XactRecord>(), XactRecordViewModel(context)))
        data.add(DataMakeViewModelView(typeOf<XactEvent>(), XactEventViewModel(context)))
        data.add(DataMakeViewModelView(typeOf<XactSector>(), XactSectorViewModel(context)))

        // Setting
        data.add(DataMakeViewModelView(typeOf<SettingUser>(), SettingUserViewModel(context)))
        data.add(DataMakeViewModelView(typeOf<SettingMenu>(), SettingMenuViewModel(context)))
        data.add(DataMakeViewModelView(typeOf<SettingProfile>(), SettingProfileViewModel(context)))
        data.add(DataMakeViewModelView(typeOf<SettingProfileUser>(), SettingProfileUserViewModel(context)))
        data.add(DataMakeViewModelView(typeOf<SettingProfileMenu>(), SettingProfileMenuViewModel(context)))
        return data
    }

    override fun getInstanceOfIViewModelView(
        type: KType,
        context: Application
    ): IViewModelView<*>? {
        val temp = storedInstanceOfViewModelView(context)
            .filter { it.viewModel is IViewModelView<*> }
            .firstOrNull { it.isType(type) }

        return when (temp == null) {
            true -> null
            false -> temp.viewModel as IViewModelView<*>
        }
    }

    override fun getInstanceOfIViewModelAllSimpleListIdRelation(
        type: KType,
        context: Application
    ): IViewModelAllSimpleListIdRelation<*>? {
        val temp = storedInstanceOfViewModelView(context)
            .filter { it.viewModel is IViewModelAllSimpleListIdRelation<*> }
            .firstOrNull { it.isType(type) }

        return when (temp == null) {
            true -> null
            false -> temp.viewModel as IViewModelAllSimpleListIdRelation<*>
        }
    }

    companion object {

    }
}