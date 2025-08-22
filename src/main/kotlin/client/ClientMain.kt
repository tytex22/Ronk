package client

import client.network.SecureClientConnection
import client.features.app.AppUI
import client.features.authorization.AuthUI

fun main() {
    SecureClientConnection.start()
    Thread.sleep(2000) // wait for connection

    AuthUI.apply {
        setOnSignInSuccess {
            AppUI.isVisible = true
        }
        isVisible = true
    }

}