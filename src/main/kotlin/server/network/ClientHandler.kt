package server.network

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import server.api.AuthApi
import shared.protocol.Command
import shared.protocol.Request
import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.AuthData
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

object ClientHandler {

    fun handle(clientSocket: Socket) {
        println("Client connected: ${clientSocket.inetAddress.hostAddress}")
        val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        val writer = PrintWriter(clientSocket.getOutputStream(), true)

        try {
            Thread.currentThread().name = "Client-${clientSocket.inetAddress}"

            while (true) {
                val line = reader.readLine() ?: break
                println("Received from client: $line")

                val request = try {
                    Json.decodeFromString<Request>(line)
                } catch (e: Exception) {
                    writer.println(Response(Status.ERROR, "invalid request: $e"))
                    return
                }

                val response = when(request.command) {
                    Command.SIGN_IN -> AuthApi.handleSignIn(request.data as AuthData)
                    Command.SIGN_UP -> AuthApi.handleSignUp(request.data as AuthData)
                }
                print(response)
                writer.println(Json.encodeToString(response))
            }

        } catch (e: Exception) {
            writer.println(Response(Status.ERROR, "Server Error: ${e.message}"))
            println("Error handling client: ${e.message}")
        } finally {
            println("Client disconnected: ${clientSocket.inetAddress.hostAddress}")
            clientSocket.close()
        }
    }

}

