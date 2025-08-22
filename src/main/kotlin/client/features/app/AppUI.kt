package client.features.app

import client.network.SecureClientConnection
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.SwingConstants

object AppUI : JFrame("Main Application") {
    init {
        setSize(800, 600)
        defaultCloseOperation = EXIT_ON_CLOSE
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                SecureClientConnection.close() // clean up your client connection here
                // then JFrame will close because of EXIT_ON_CLOSE
            }
        })
        setLocationRelativeTo(null)

        // Simple welcome message
        add(JLabel("Welcome to the App!", SwingConstants.CENTER))
    }
}