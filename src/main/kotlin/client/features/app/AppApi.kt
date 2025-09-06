package client.features.app

import client.network.SecureClientConnection
import shared.protocol.Command
import shared.protocol.Request
import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.ClassesList

object AppApi {
    fun getClassesList(): List<String> {

        val request = Request(Command.GET_CLASSES_LIST)
        val response: Response = SecureClientConnection.sendCommand(request)

        return when (response.status) {
            Status.SUCCESS -> (response.data as ClassesList).classesList
            Status.ERROR -> listOf("${response.message}")
            Status.FAIL -> listOf("${response.message}")
        }
    }

}