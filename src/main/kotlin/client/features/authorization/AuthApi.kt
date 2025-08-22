package client.features.authorization

import client.network.SecureClientConnection
import shared.protocol.Command
import shared.protocol.Request
import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.AuthData

object AuthApi {
    fun signIn(userName: String, password: String): /*Response*/ Pair<Boolean, String?> {

        if (userName.isBlank() || password.isBlank()) {
            return false to "username or password is blank."
        }

        val request = Request(Command.SIGN_IN, AuthData(userName, password))
        val response: Response = SecureClientConnection.sendCommand(request)

        return when (response.status) {
            Status.SUCCESS -> true to "signed in successfully!"
            else -> false to (response.message ?: "Sign in failed")
        }
    }

    fun signUp(userName: String, password: String,  password2: String): /*Response*/ Pair<Boolean, String?> {

        if (userName.isBlank() || password.isBlank() || password2.isBlank()) return false to "Username or password is blank!"
        if (password != password2) return false to "Passwords do not match"


        val request = Request(Command.SIGN_UP, AuthData(userName, password))
        val response: Response = SecureClientConnection.sendCommand(request)

        return when (response.status) {
            Status.SUCCESS -> true to "signed up successfully!"
            else -> false to (response.message ?: "Signup failed")
        }

    }
}