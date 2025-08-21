package client.network

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import shared.protocol.Request
import shared.protocol.Response
import shared.protocol.Status
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.TrustManagerFactory

object SecureClientConnection {
    private lateinit var socket: Socket
    private lateinit var writer: PrintWriter
    private lateinit var reader: BufferedReader
    var connected: Boolean = false
        private set

    fun start() {
        try {
            socket = startSecureConnection()
            writer = PrintWriter(socket.getOutputStream(), true)
            reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            connected = true
            println("Connected to server!")
        } catch (e: Exception) {
            println("Connection error: ${e.message}")
        }
    }

    fun sendCommand(request: Request): Response {
        if (!connected) {
            println("Not connected — cannot send command.")
            return Response(Status.ERROR, "Not connected")
        }

        return try {
            writer.println(Json.encodeToString(request))
            val response: Response = Json.decodeFromString(reader.readLine())
            println(response)
            response
        } catch (e: Exception) {
            println("Invalid server response")
            Response(Status.ERROR, "Connection Error: $e")
        }
    }

    fun close() {
        try {
            if (connected) {
                writer.close()
                reader.close()
                socket.close()
                connected = false
                println("Connection closed.")
            }
        } catch (e: Exception) {
            println("Error closing connection: ${e.message}")
        }
    }
}

fun startSecureConnection(): SSLSocket {
    val host = "localhost"
    val port = 8888
    val ksPath = "certs/client_keystore.jks"
    val ksPwd = "password"
    val trustPath = "certs/client_truststore.jks"
    val trustPwd = "password"

    val ks = KeyStore.getInstance("JKS")
    ks.load(FileInputStream(ksPath), ksPwd.toCharArray())

    val ts = KeyStore.getInstance("JKS")
    ts.load(FileInputStream(trustPath), trustPwd.toCharArray())

    val kmf = KeyManagerFactory.getInstance("SunX509")
    kmf.init(ks, ksPwd.toCharArray())

    val tmf = TrustManagerFactory.getInstance("SunX509")
    tmf.init(ts)

    val ctx = SSLContext.getInstance("TLS")
    ctx.init(kmf.keyManagers, tmf.trustManagers, null)

    val socketFactory = ctx.socketFactory
    val socket = socketFactory.createSocket(host, port) as SSLSocket
//    socket.needClientAuth = true

    return socket
}
