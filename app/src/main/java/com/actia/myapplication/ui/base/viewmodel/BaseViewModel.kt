package com.actia.myapplication.ui.base.viewmodel

import android.app.Application
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@OptIn(KoinApiExtension::class)
open class BaseViewModel(application: Application) : ApplicationViewModel(application), KoinComponent {

}