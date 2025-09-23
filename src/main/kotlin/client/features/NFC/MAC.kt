package client.features.NFC
import java.net.InetAddress
import java.net.NetworkInterface

object MAC {
    fun getMacAddressBytes(): ByteArray? {
        val localHost = InetAddress.getLocalHost()
        val networkInterface = NetworkInterface.getByInetAddress(localHost) ?: return null
        return networkInterface.hardwareAddress
    }

}