package server.api

import server.service.AuthService
import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.AuthData

object AuthApi {
    fun handleSignIn(data: AuthData): Response {
        return try {
            val userName = data.userName
            val password = data.password

            AuthService.signIn(userName, password)
        } catch (e: Exception) {
            Response(Status.ERROR, e.message ?: "Invalid request")
        }
    }

    fun handleSignUp(data: AuthData): Response {
        return try {
            val userName = data.userName
            val password = data.password

            AuthService.signUp(userName, password)
        } catch (e: Exception) {
            Response(Status.ERROR, e.message ?: "Invalid request")
        }
    }
}