package client.features.NFC

import com.fazecast.jSerialComm.SerialPort

object NFC {

    fun readNFC(): ByteArray? {
        val port = SerialPort.getCommPort("/dev/ttyUSB0")
        port.baudRate = 9600
        port.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0) // block until data
        if (!port.openPort()) {
            println("Failed to open port.")
            return null
        }

        // give Arduino time to reset after opening serial
        Thread.sleep(1500)

        println("Listening for RFID UIDs...")

        val reader = port.inputStream.bufferedReader()
        while (true) {
            val line = reader.readLine()?.trim()
            if (!line.isNullOrEmpty()) {
                println("Card detected -> $line")
                if (line != "Scan a card...") return hexStringToByteArray(line)
                // here you can call hexStringToByteArray(line) and insert into DB
            }
        }
    }

    private fun findUSBPort(): SerialPort = TODO()

    private fun hexStringToByteArray(s: String): ByteArray {
        val cleaned = s.trim().uppercase()
        return ByteArray(cleaned.length / 2) { i ->
            cleaned.substring(2 * i, 2 * i + 2).toInt(16).toByte()
        }
    }
}

//fun main() {
//    val hex: ByteArray? = NFC.getUID()
//    if (hex != null) println("hex: $hex") else println("null")
//}