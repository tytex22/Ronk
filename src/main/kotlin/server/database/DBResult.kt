package server.database


import shared.protocol.Response
import shared.protocol.data.ServerData

sealed class DBResult {
    class ResponseOnly(val response: Response) : DBResult()
    class WithData(val data: ServerData) : DBResult()
}
