package client.features.app

import client.features.NFC.NFC.getUID
import client.network.SecureClientConnection
import java.awt.*
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

//       Create a vertical BoxLayout panel
////        val panel = JPanel()
////        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
////
////        // Welcome label (centered)
////        val welcomeLabel = JLabel("Welcome to the App!")
////        welcomeLabel.alignmentX = Component.CENTER_ALIGNMENT
////        panel.add(welcomeLabel)
////
////        // Class list labels (also centered)
////        val classesList = AppApi.getClassesList()
////        println(classesList)
////        classesList.forEach {
////            val classLabel = JLabel("Class: $it")
////            classLabel.alignmentX = Component.CENTER_ALIGNMENT
////            panel.add(classLabel)
////        }
////
////        // Optional: add some spacing
////        panel.border = BorderFactory.createEmptyBorder(20, 20, 20, 20)
////
////        contentPane.add(panel)*/
        setupUI()
//        loadClasses()

    }



    private fun setupUI() {
        layout = BorderLayout()

        // Main panel
        val mainPanel = JPanel(BorderLayout())

        // Welcome label at the top
        val welcomeLabel = JLabel("Welcome to the App!", SwingConstants.CENTER).apply {
            font = Font("Arial", Font.BOLD, 20)
            border = BorderFactory.createEmptyBorder(20, 20, 20, 20)
        }
        mainPanel.add(welcomeLabel, BorderLayout.NORTH)

        // Grid panel for class buttons (will be populated in loadClasses)
        val gridPanel = JPanel(GridLayout(0, 3, 15, 15)).apply {
            border = BorderFactory.createEmptyBorder(20, 20, 20, 20)
        }
        // 3 columns, dynamic rows

        val scrollPane = JScrollPane(gridPanel)
        mainPanel.add(scrollPane, BorderLayout.CENTER)

        // Bottom panel for scan button and notifications
        val bottomPanel = JPanel(BorderLayout())
        val scanButton = JButton("Scan NFC").apply {
            preferredSize = Dimension(120, 40)
            addActionListener { getUID() }
        }

        val buttonPanel = JPanel(FlowLayout())
        buttonPanel.add(scanButton)
        bottomPanel.add(buttonPanel, BorderLayout.CENTER)

        mainPanel.add(bottomPanel, BorderLayout.SOUTH)
        contentPane.add(mainPanel)

        // Store reference to grid panel for later use
        gridPanel.name = "gridPanel"
    }

//    private fun loadClasses() {
//        val gridPanel = findGridPanel()
//        gridPanel.removeAll()
//
//        try {
//            val classesList = AppApi.getClassesList()
//            println("Loaded classes: $classesList")
//
//            if (classesList.isEmpty()) {
//                val emptyLabel = JLabel("No classes found", SwingConstants.CENTER)
//                emptyLabel.foreground = Color.GRAY
//                gridPanel.add(emptyLabel)
//            } else {
//                classesList.forEach { className ->
//                    val button = JButton(className)
//                    button.preferredSize = Dimension(200, 80)
//                    button.font = Font("Arial", Font.PLAIN, 14)
//
//                    // Add action listener to open student list
//                    button.addActionListener {
//                        openStudentsList(className)
//                    }
//
//                    gridPanel.add(button)
//                }
//            }
//        } catch (e: Exception) {
//            val errorLabel = JLabel("Error loading classes: ${e.message}", SwingConstants.CENTER)
//            errorLabel.foreground = Color.RED
//            gridPanel.add(errorLabel)
//        }
//
//        gridPanel.revalidate()
//        gridPanel.repaint()
//    }
//
//    private fun findGridPanel(): JPanel {
//        return findComponentByName(contentPane, "gridPanel") as JPanel
//    }
//
//    private fun findComponentByName(parent: Container, name: String): Component? {
//        for (component in parent.components) {
//            if (component.name == name) {
//                return component
//            }
//            if (component is Container) {
//                val found = findComponentByName(component, name)
//                if (found != null) return found
//            }
//        }
//        return null
//    }
//
//    private fun openStudentsList(className: String) {
//        // Close existing student window if open
//        studentsWindow?.dispose()
//
//        studentsWindow = JFrame("Students in $className")
//        studentsWindow!!.setSize(600, 500)
//        studentsWindow!!.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
//        studentsWindow!!.setLocationRelativeTo(this)
//        studentsWindow!!.layout = BorderLayout()
//
//        // Title
//        val titleLabel = JLabel("Students in: $className", SwingConstants.CENTER)
//        titleLabel.font = Font("Arial", Font.BOLD, 16)
//        titleLabel.border = BorderFactory.createEmptyBorder(15, 15, 15, 15)
//        studentsWindow!!.add(titleLabel, BorderLayout.NORTH)
//
//        // Loading panel
//        val loadingPanel = JPanel(FlowLayout())
//        val loadingLabel = JLabel("Loading students...")
//        val progressBar = JProgressBar()
//        progressBar.isIndeterminate = true
//        loadingPanel.add(loadingLabel)
//        loadingPanel.add(progressBar)
//        studentsWindow!!.add(loadingPanel, BorderLayout.CENTER)
//
//        studentsWindow!!.isVisible = true
//
//        // Load students in background thread
//        SwingWorker<List<String>, Void>().apply {
//            override fun doInBackground(): List<String> {
//                return try {
//                    // Replace with your actual API call to get students for the class
//                    AppApi.getStudentsForClass(className)
//                } catch (e: Exception) {
//                    println("Error loading students: ${e.message}")
//                    emptyList()
//                }
//            }
//
//            override fun done() {
//                try {
//                    val students = get()
//                    studentsWindow!!.remove(loadingPanel)
//
//                    if (students.isNotEmpty()) {
//                        displayStudentsTable(students)
//                    } else {
//                        val noStudentsLabel = JLabel("No students found in this class", SwingConstants.CENTER)
//                        noStudentsLabel.foreground = Color.GRAY
//                        studentsWindow!!.add(noStudentsLabel, BorderLayout.CENTER)
//                    }
//
//                    studentsWindow!!.revalidate()
//                    studentsWindow!!.repaint()
//                } catch (e: Exception) {
//                    studentsWindow!!.remove(loadingPanel)
//                    val errorLabel = JLabel("Error: ${e.message}", SwingConstants.CENTER)
//                    errorLabel.foreground = Color.RED
//                    studentsWindow!!.add(errorLabel, BorderLayout.CENTER)
//                    studentsWindow!!.revalidate()
//                    studentsWindow!!.repaint()
//                }
//            }
//        }.execute()
//    }
//
//    private fun displayStudentsTable(students: List<String>) {
//        val columnNames = arrayOf("Student Name", "Entry Time", "Status")
//        val tableData = students.map { studentName ->
//            arrayOf(studentName, "09:30 AM", "Present") // Replace with actual data
//        }.toTypedArray()
//
//        val table = JTable(tableData, columnNames)
//        table.fillsViewportHeight = true
//        table.font = Font("Arial", Font.PLAIN, 12)
//
//        // Set column widths
//        table.columnModel.getColumn(0).preferredWidth = 200
//        table.columnModel.getColumn(1).preferredWidth = 100
//        table.columnModel.getColumn(2).preferredWidth = 100
//
//        val scrollPane = JScrollPane(table)
//        studentsWindow!!.add(scrollPane, BorderLayout.CENTER)
//
//        // Add refresh button
//        val buttonPanel = JPanel(FlowLayout())
//        val refreshButton = JButton("Refresh")
//        refreshButton.addActionListener {
//            // Reload the student list
//            studentsWindow!!.dispose()
//            openStudentsList(students.toString()) // You might want to store className differently
//        }
//        buttonPanel.add(refreshButton)
//        studentsWindow!!.add(buttonPanel, BorderLayout.SOUTH)
//    }
//
//    private fun startNFCScan() {
//        clearNotification()
//
//        try {
//            // Simulate NFC scanning - replace with your actual NFC implementation
//            val studentId = simulateNFCScan()
//
//            if (studentId != null) {
//                processScannedStudent(studentId)
//            } else {
//                showNotification("NFC device not connected or no card detected", Color.ORANGE)
//            }
//        } catch (e: Exception) {
//            showNotification("NFC scan failed: ${e.message}", Color.RED)
//        }
//    }
//
//    private fun simulateNFCScan(): String? {
//        // Replace this with your actual NFC scanning logic
//        val options = arrayOf("Simulate Scan", "Cancel")
//        val result = JOptionPane.showOptionDialog(
//            this,
//            "Simulate NFC scan?",
//            "NFC Scanner",
//            JOptionPane.YES_NO_OPTION,
//            JOptionPane.QUESTION_MESSAGE,
//            null,
//            options,
//            options[0]
//        )
//
//        return if (result == 0) "STUDENT_ID_${System.currentTimeMillis()}" else null
//    }
//
//    private fun processScannedStudent(studentId: String) {
//        // Check if student exists in system
//        try {
//            val studentExists = AppApi.checkStudentExists(studentId)
//
//            if (studentExists) {
//                showNotification("Student checked in successfully!", Color.GREEN)
//                // Optionally refresh any open student windows
//            } else {
//                // Show dialog to add new student
//                showAddStudentDialog(studentId)
//            }
//        } catch (e: Exception) {
//            showNotification("Error processing student: ${e.message}", Color.RED)
//        }
//    }
//
//    private fun showAddStudentDialog(studentId: String) {
//        addStudentDialog = JDialog(this, "Add New Student", true)
//        addStudentDialog!!.setSize(400, 200)
//        addStudentDialog!!.setLocationRelativeTo(this)
//        addStudentDialog!!.layout = GridBagLayout()
//
//        val gbc = GridBagConstraints()
//        gbc.insets = Insets(10, 10, 10, 10)
//
//        // Title
//        gbc.gridx = 0
//        gbc.gridy = 0
//        gbc.gridwidth = 2
//        val titleLabel = JLabel("Student not found. Add to system?")
//        titleLabel.font = Font("Arial", Font.BOLD, 14)
//        addStudentDialog!!.add(titleLabel, gbc)
//
//        // Student name field
//        gbc.gridy = 1
//        gbc.gridwidth = 1
//        addStudentDialog!!.add(JLabel("Student Name:"), gbc)
//
//        gbc.gridx = 1
//        val nameField = JTextField(20)
//        addStudentDialog!!.add(nameField, gbc)
//
//        // Buttons
//        gbc.gridx = 0
//        gbc.gridy = 2
//        val addButton = JButton("Add Student")
//        addButton.addActionListener {
//            val name = nameField.text.trim()
//            if (name.isNotEmpty()) {
//                try {
//                    AppApi.addNewStudent(studentId, name)
//                    showNotification("Student '$name' added successfully!", Color.GREEN)
//                    addStudentDialog!!.dispose()
//                } catch (e: Exception) {
//                    JOptionPane.showMessageDialog(
//                        addStudentDialog,
//                        "Error adding student: ${e.message}",
//                        "Error",
//                        JOptionPane.ERROR_MESSAGE
//                    )
//                }
//            } else {
//                JOptionPane.showMessageDialog(
//                    addStudentDialog,
//                    "Please enter a student name",
//                    "Validation Error",
//                    JOptionPane.WARNING_MESSAGE
//                )
//            }
//        }
//        addStudentDialog!!.add(addButton, gbc)
//
//        gbc.gridx = 1
//        val cancelButton = JButton("Cancel")
//        cancelButton.addActionListener { addStudentDialog!!.dispose() }
//        addStudentDialog!!.add(cancelButton, gbc)
//
//        addStudentDialog!!.isVisible = true
//    }
//
//    private fun showNotification(message: String, color: Color) {
//        clearNotification()
//
//        notificationLabel = JLabel(message, SwingConstants.CENTER)
//        notificationLabel!!.foreground = color
//        notificationLabel!!.font = Font("Arial", Font.BOLD, 12)
//        notificationLabel!!.border = BorderFactory.createEmptyBorder(5, 10, 10, 10)
//
//        // Find the main panel and add notification at bottom
//        val mainPanel = contentPane.components[0] as JPanel
//        val bottomPanel = mainPanel.components.find { it is JPanel && (it as JPanel).components.any { c -> c is JButton } } as JPanel?
//
//        bottomPanel?.add(notificationLabel, BorderLayout.NORTH)
//        revalidate()
//        repaint()
//
//        // Auto-clear notification after 5 seconds
//        Timer(5000) {
//            clearNotification()
//        }.apply {
//            isRepeats = false
//            start()
//        }
//    }
//
//    private fun clearNotification() {
//        notificationLabel?.let { label ->
//            label.parent?.remove(label)
//            revalidate()
//            repaint()
//        }
//        notificationLabel = null
//    }
//
//    fun refreshClasses() {
//        loadClasses()
//    }
}

