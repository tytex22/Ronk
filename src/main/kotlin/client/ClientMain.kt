package client

import client.features.app.AppUI
import client.network.SecureClientConnection

fun main() {
    SecureClientConnection.start()
    Thread.sleep(2000) // wait for connection

//    AuthUI.apply {
//        setOnSignInSuccess {
//            AppUI.isVisible = true
//        }
//        isVisible = true
//    }
    AppUI.isVisible = true
}