package client.features.app

import client.network.SecureClientConnection
import java.awt.Component
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*

object AppUI : JFrame("Main Application") {
    init {
        setSize(800, 600)
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)

        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                SecureClientConnection.close()
            }
        })

        // Create a vertical BoxLayout panel
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        // Welcome label (centered)
        val welcomeLabel = JLabel("Welcome to the App!")
        welcomeLabel.alignmentX = Component.CENTER_ALIGNMENT
        panel.add(welcomeLabel)

        // Class list labels (also centered)
        val classesList = AppApi.getClassesList()
        println(classesList)
        classesList.forEach {
            val classLabel = JLabel("Class: $it")
            classLabel.alignmentX = Component.CENTER_ALIGNMENT
            panel.add(classLabel)
        }

        // Optional: add some spacing
        panel.border = BorderFactory.createEmptyBorder(20, 20, 20, 20)

        contentPane.add(panel)
    }
}
