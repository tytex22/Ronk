package client

import client.network.SecureClientConnection
import client.ui.AppWindow
import client.ui.AuthWindow

fun main() {
    SecureClientConnection.start()
    Thread.sleep(2000) // wait for connection

    AuthWindow.apply {
        setOnSignInSuccess {
            AppWindow.isVisible = true
        }
        isVisible = true
    }

}