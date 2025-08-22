package server.features.authorization

import server.features.authorization.AuthService
import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.AuthData

object AuthApi {
    fun handleSignIn(data: AuthData): Response {
        try {
            val userName = data.userName
            val password = data.password

            return AuthService.signIn(userName, password)
        } catch (e: Exception) {
            return Response(Status.ERROR, e.message ?: "Invalid request")
        }
    }

    fun handleSignUp(data: AuthData): Response {
        try {
            val userName = data.userName
            val password = data.password

            return AuthService.signUp(userName, password)
        } catch (e: Exception) {
            return Response(Status.ERROR, e.message ?: "Invalid request")
        }
    }
}