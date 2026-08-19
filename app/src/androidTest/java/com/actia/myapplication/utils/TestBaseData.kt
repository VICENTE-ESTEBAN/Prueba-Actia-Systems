package com.actia.myapplication.utils

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.actia.myapplication.di.AppModules
import com.actia.myapplication.di.DomainModules
import com.actia.myapplication.di.RepositoryModules
import com.actia.myapplication.util.IdlingResourceCounter
import mockwebserver3.MockWebServer
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.koinApplication

abstract class TestBaseData: KoinComponent {

    protected fun disableAnimations() {
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.executeShellCommand("settings put global window_animation_scale 0")
        uiDevice.executeShellCommand("settings put global transition_animation_scale 0")
        uiDevice.executeShellCommand("settings put global animator_duration_scale 0")
    }

    protected fun enableAnimations() {
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.executeShellCommand("settings put global window_animation_scale 1")
        uiDevice.executeShellCommand("settings put global transition_animation_scale 1")
        uiDevice.executeShellCommand("settings put global animator_duration_scale 1")
    }

    protected fun koinTestSetUp() {
        //FIRST stop koin from application
        stopKoin()

        IdlingResourceCounter.reset()

        val diApp = koinApplication {
            allowOverride(true)
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
            modules(AppModules().get())
        }

        startKoin(koinApplication = diApp)
    }

    fun runningMockWebServer(serverClosure: (MockWebServer) -> Unit) {
        val mockWebServer = MockWebServer()
        mockWebServer.start()

        // Ya que el caso de uso utilizará Koin para inyectar la dependencia con el SARepositoryAPI,
        // debemos sustituir el repositorio por el objeto Mockeado
        val url = mockWebServer.url("/").toString()//val url = mockWebServer.url("/").toString().replace("localhost", "10.0.2.2")
        val repoModule: List<Module> = RepositoryModules(url).get()
        // Al cargar otra vez el módulo, Koin realiza un override del mismo
        loadKoinModules(repoModule)
        loadKoinModules(DomainModules().get())

        mockWebServer.use { mockWebServer ->
            serverClosure(mockWebServer)
        }
    }
}
