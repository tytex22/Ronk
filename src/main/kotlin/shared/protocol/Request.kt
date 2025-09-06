package shared.protocol

import kotlinx.serialization.Serializable
import shared.protocol.data.ClientData

@Serializable
data class Request(val command: Command, val data: ClientData? = null)
