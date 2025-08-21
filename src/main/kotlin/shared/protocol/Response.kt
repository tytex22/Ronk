package shared.protocol

import kotlinx.serialization.Serializable
import shared.protocol.data.ServerData

@Serializable
data class Response(val status: Status, val message: String? = null, val data: ServerData? = null)