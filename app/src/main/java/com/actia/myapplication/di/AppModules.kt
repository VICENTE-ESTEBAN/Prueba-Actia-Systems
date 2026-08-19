package com.actia.myapplication.di

import com.actia.myapplication.data.domain.usecase.GetDetailItemByImdbUseCase
import com.actia.myapplication.data.domain.usecase.GetDetailItemByTitleUseCase
import com.actia.myapplication.data.domain.usecase.GetItemsUseCase
import com.actia.myapplication.ui.main.viewmodel.MainViewModel
import com.actia.myapplication.ui.main.viewmodel.MainViewModel1
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

class AppModules : KoinModules {
    override fun get(): List<Module> {
        return listOf(getViewModels())
    }

    private fun getViewModels() = module {
        viewModel<MainViewModel> { MainViewModel(androidApplication(),
            getItemsUseCase =  get<GetItemsUseCase>(),
            getDetailItemByImdbUseCase = get<GetDetailItemByImdbUseCase>(),
            getDetailItemByTitleUseCase = get<GetDetailItemByTitleUseCase>() ) }


        viewModel<MainViewModel1> { MainViewModel1(
            application = androidApplication(),
            getItemsUseCase =  get<GetItemsUseCase>(),
            getDetailItemByImdbUseCase = get<GetDetailItemByImdbUseCase>(),
            getDetailItemByTitleUseCase = get<GetDetailItemByTitleUseCase>() ) }
    }
}