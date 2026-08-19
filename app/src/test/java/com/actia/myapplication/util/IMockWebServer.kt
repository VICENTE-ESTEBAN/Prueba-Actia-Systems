package com.actia.myapplication.util

import mockwebserver3.MockWebServer


interface IMockWebServer {

    fun runningMockWebServer(serverClosure: (MockWebServer) -> Unit) {
        val mockWebServer = MockWebServer()

        serverClosure(mockWebServer)

        // Finish web server
        mockWebServer.close()
    }
}