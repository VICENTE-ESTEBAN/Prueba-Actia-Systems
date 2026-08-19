package com.actia.myapplication

import com.actia.myapplication.di.AppModules
import com.actia.myapplication.di.DomainModules
import com.actia.myapplication.di.RepositoryModules
import mockwebserver3.MockWebServer
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.koinApplication

abstract class TestBaseData: KoinComponent {

    protected fun koinTestSetUp() {
        //FIRST stop koin from application
        stopKoin()

        val diApp = koinApplication {
            androidContext(ApplicationActia())
            modules(AppModules().get())
        }

        startKoin(koinApplication = diApp)
    }

    fun runningMockWebServer(serverClosure: (MockWebServer) -> Unit) {
        val mockWebServer = MockWebServer()
        mockWebServer.start()

        // Ya que el caso de uso utilizará Koin para inyectar la dependencia con el SARepositoryAPI,
        // debemos sustituir el repositorio por el objeto Mockeado
        val repoModule: List<Module> = RepositoryModules(mockWebServer.url("/").toString()).get()
        // Al cargar otra vez el módulo, Koin realiza un override del mismo
        loadKoinModules(repoModule)
        loadKoinModules(DomainModules().get())

        try {
            serverClosure(mockWebServer)
        }
        finally {
            // Finish web server
            mockWebServer.close()
        }
    }

    protected fun koinTestClose() {
        stopKoin()
    }
}
