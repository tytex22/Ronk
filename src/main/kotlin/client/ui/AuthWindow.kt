package client.ui

import client.network.SecureClientConnection
import client.service.AuthService
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JPasswordField
import javax.swing.JTextField
import javax.swing.SwingConstants
import javax.swing.SwingUtilities

object AuthWindow : JFrame("Main Application"){
    private var onSignInSuccess: (() -> Unit)? = null

    fun setOnSignInSuccess(listener: () -> Unit) {
        this.onSignInSuccess = listener
    }

    private val cardLayout = CardLayout()
    private val cardPanel = JPanel(cardLayout)

    // Add these at the top of your AuthWindow class
    // For sign-in
    private lateinit var signInUserNameInput: JTextField
    private lateinit var signInPasswordInput: JPasswordField

    // For sign-up
    private lateinit var signUpUserNameInput: JTextField
    private lateinit var signUpPasswordInput: JPasswordField
    private lateinit var signUpPasswordInput2: JPasswordField

    // Shared error label per panel
    private lateinit var signInErrorLabel: JLabel
    private lateinit var signUpErrorLabel: JLabel


    init {
        setSize(200, 400)
        isResizable = false
        // Set default close operation
        defaultCloseOperation = EXIT_ON_CLOSE
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                SecureClientConnection.close() // clean up your client connection here
                // then JFrame will close because of EXIT_ON_CLOSE
            }
        })
        // Center the window
        setLocationRelativeTo(null)
//            layout = GridBagLayout()

        val signInPanel = createSignInPanel()
        val signUpPanel = createSignUpPanel()

        // Add a simple label
        cardPanel.add(signInPanel, "signIn")
        cardPanel.add(signUpPanel, "signUp")

        // Make it visible
        add(cardPanel)
        isVisible = true
    }

    private fun createSignInPanel(): JPanel {
        val frameLabel = newLabel("SIGN IN")
        val userNameLabel = newLabel("type your username")
        signInUserNameInput = newTextInput()
        val passwordLabel = newLabel("type your user password")
        signInPasswordInput = newPasswordInput()
        signInErrorLabel = newLabel("")

        val signInButton = JButton("Sign In").apply {
            alignmentX = CENTER_ALIGNMENT
            addActionListener {
                val result = AuthService.handleSignIn(
                    signInUserNameInput.text,
                    String(signInPasswordInput.password)
                )
                if (result.first) {
                    signInErrorLabel.text = result.second
                    onSignInSuccess?.invoke()
                    dispose()
                } else {
                    signInErrorLabel.text = result.second
                }
            }
        }

        val switchButton = JButton("Don't have an account?").apply {
            alignmentX = CENTER_ALIGNMENT
            addActionListener {
                clearSignUpFields()
                cardLayout.show(cardPanel, "signUp")
            }
        }

        val mainPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(Box.createVerticalStrut(10))
            add(frameLabel)
            add(Box.createVerticalStrut(20))
            add(userNameLabel)
            add(Box.createVerticalStrut(5))
            add(signInUserNameInput)
            add(Box.createVerticalStrut(10))
            add(passwordLabel)
            add(Box.createVerticalStrut(5))
            add(signInPasswordInput)
            add(Box.createVerticalStrut(10))
            add(signInButton)
            add(Box.createVerticalStrut(10))
            add(signInErrorLabel)
            add(Box.createVerticalStrut(10))
        }

        val switchPanel = JPanel(FlowLayout(FlowLayout.CENTER)).apply {
            add(switchButton)
        }

        val panel = JPanel(BorderLayout()).apply {
            add(switchPanel, BorderLayout.SOUTH)
            add(mainPanel, BorderLayout.CENTER)
        }

        return panel
    }

    private fun createSignUpPanel(): JPanel {
        val frameLabel = newLabel("SIGN UP")
        val userNameLabel = newLabel("type your username")
        signUpUserNameInput = newTextInput()
        val passwordLabel = newLabel("type your user password")
        signUpPasswordInput = newPasswordInput()
        val passwordLabel2 = newLabel("type your user again")
        signUpPasswordInput2 = newPasswordInput()
        signUpErrorLabel = newLabel("")

        val signInButton = JButton("Sign Up").apply {
            alignmentX = CENTER_ALIGNMENT
            addActionListener {
                val result = AuthService.handleSignUp(
                    signUpUserNameInput.text,
                    String(signUpPasswordInput.password),
                    String(signUpPasswordInput2.password)
                )
                if (result.first) {
                    SwingUtilities.invokeLater {

                        JOptionPane.showMessageDialog(
                            signUpPasswordInput,
                            "Account created successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        )
                        cardLayout.show(cardPanel, "signIn")
                    }
                } else {
                    signUpErrorLabel.text = result.second
                }
            }
        }

        val switchButton = JButton("Already have an account?").apply {
            alignmentX = CENTER_ALIGNMENT
            addActionListener {
                clearSignInFields()
                cardLayout.show(cardPanel, "signIn")
            }
        }

        val mainPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(Box.createVerticalStrut(10))
            add(frameLabel)
            add(Box.createVerticalStrut(20))
            add(userNameLabel)
            add(Box.createVerticalStrut(5))
            add(signUpUserNameInput)
            add(Box.createVerticalStrut(10))
            add(passwordLabel)
            add(Box.createVerticalStrut(5))
            add(signUpPasswordInput)
            add(Box.createVerticalStrut(10))
            add(passwordLabel2)
            add(Box.createVerticalStrut(5))
            add(signUpPasswordInput2)
            add(Box.createVerticalStrut(10))
            add(signInButton)
            add(Box.createVerticalStrut(10))
            add(signUpErrorLabel)
            add(Box.createVerticalStrut(10))
        }

        val switchPanel = JPanel(FlowLayout(FlowLayout.CENTER)).apply {
            add(switchButton)
        }

        val panel = JPanel(BorderLayout()).apply {
            add(mainPanel, BorderLayout.CENTER)
            add(switchPanel, BorderLayout.SOUTH)
        }

        return panel
    }


    private fun newLabel(text: String): JLabel = JLabel(text).apply {
        alignmentX = CENTER_ALIGNMENT
        horizontalAlignment = SwingConstants.CENTER
    }

    private fun newTextInput(): JTextField = JTextField().apply {
        alignmentX = CENTER_ALIGNMENT
        maximumSize = Dimension(180, preferredSize.height)
    }

    private fun newPasswordInput(): JPasswordField = JPasswordField().apply {
        alignmentX = CENTER_ALIGNMENT
        maximumSize = Dimension(180, preferredSize.height)
    }

    private fun clearSignInFields() {
        signInUserNameInput.text = ""
        signInPasswordInput.text = ""
        signInErrorLabel.text = ""
    }

    private fun clearSignUpFields() {
        signUpUserNameInput.text = ""
        signUpPasswordInput.text = ""
        signUpPasswordInput2.text = ""
        signUpErrorLabel.text = ""
    }
}