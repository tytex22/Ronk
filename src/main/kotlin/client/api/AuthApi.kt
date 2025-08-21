package client.api

import client.network.SecureClientConnection
import shared.protocol.Command
import shared.protocol.Request
import shared.protocol.Response
import shared.protocol.data.AuthData

object AuthApi {
    fun signIn(username: String, password: String): Response {
        val request = Request(Command.SIGN_IN, AuthData(username, password))
        val response: Response = SecureClientConnection.sendCommand(request)
        return response
    }
    fun signUp(username: String, password: String): Response {
        val request = Request(Command.SIGN_UP, AuthData(username, password))
        val response: Response = SecureClientConnection.sendCommand(request)
        return response

    }
}