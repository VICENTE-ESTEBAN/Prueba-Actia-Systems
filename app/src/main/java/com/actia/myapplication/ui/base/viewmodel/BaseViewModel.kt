package com.actia.myapplication.ui.base.viewmodel

import android.app.Application
import org.koin.core.component.KoinComponent

open class BaseViewModel(application: Application) : ApplicationViewModel(application), KoinComponent {

}