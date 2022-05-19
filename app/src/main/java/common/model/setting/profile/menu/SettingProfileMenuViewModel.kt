package com.vsg.ot.common.model.setting.profile.menu

import android.app.Application
import androidx.lifecycle.LiveData
import com.vsg.helper.common.model.viewModel.ViewModelGeneric
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleList
import com.vsg.helper.common.util.viewModel.IViewModelAllTextSearch
import com.vsg.helper.common.util.viewModel.IViewModelHasItemsRelation
import com.vsg.helper.common.util.viewModel.IViewModelView
import com.vsg.ot.common.data.AppDatabase
import common.model.init.viewModel.ViewModelStoredMap
import common.model.master.item.MasterItemViewModel
import kotlinx.coroutines.runBlocking

@ExperimentalStdlibApi
class SettingProfileMenuViewModel(application: Application) :
    ViewModelGeneric<SettingProfileMenuDao, SettingProfileMenu>(
        AppDatabase.getInstance(application)?.settingProfileMenuDao()!!,
        application,
        ViewModelStoredMap()
    ),
    IViewModelAllTextSearch,
    IViewModelView<SettingProfileMenu>,
    IViewModelAllSimpleList<SettingProfileMenu>,
    IViewModelHasItemsRelation {

    override fun viewModelGetAllTextSearch(): LiveData<List<String>> = runBlocking {
        return@runBlocking dao.viewGetAllTextSearch()
    }

    override fun viewModelViewAllSimpleList(): List<SettingProfileMenu> =
        dao.viewAllSimpleList() ?: listOf()

    override fun viewModelViewHasItems(idRelation: Int): Boolean =
        MasterItemViewModel(context).viewModelViewHasItems(idRelation)
}