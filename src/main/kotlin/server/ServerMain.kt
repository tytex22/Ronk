package server

import server.network.ClientHandler
import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.*

private var serverSocket: SSLServerSocket? = null
private var running = true

fun main() {
    val serverSocket = startSecureServer()

    Runtime.getRuntime().addShutdownHook(Thread {
        stop()
    })

    while (running) {
        try {
        val clientSocket = serverSocket.accept()
        Thread { ClientHandler.handle(clientSocket) }.start()
        }
        catch (e: Exception) {
            println(e.message)
        }
    }
}

fun startSecureServer(): SSLServerSocket {
    val port = 8888
    val ksPath = "certs/server_keystore.jks"
    val ksPwd = "password"
    val trustPath = "certs/server_truststore.jks"
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

    val serverSocketFactory = ctx.serverSocketFactory as SSLServerSocketFactory
    val serverSocket = serverSocketFactory.createServerSocket(port) as SSLServerSocket
    serverSocket.needClientAuth = true

    println("Server listening on port $port")

    return serverSocket
}

fun stop() {
    println("Stopping server...")
    running = false
    try {
        serverSocket?.close()
    } catch (_: Exception) {}
}
