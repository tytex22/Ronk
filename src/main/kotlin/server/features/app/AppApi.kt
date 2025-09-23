package server.features.app

import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.AddNewData

object AppApi {
    fun handleGetRoomList(): Response {
        return try {
            AppService.getRoomList()
        } catch (e: Exception) {
            Response(Status.ERROR, e.message ?: "Invalid request")
        }
    }

    fun handleGetAttendanceList(roomName: String): Response = TODO()

    fun handleCheckInStudent(roomID: String, studentID: ByteArray): Response = TODO()

    fun handleAddNewStudent(data: AddNewData): Response {
        try {
            val studentID: ByteArray = data.id
            val studentName: String = data.name

            return AppService.addNewStudent(studentName, studentID)

        } catch (e: Exception) {
            return Response(Status.ERROR, e.message ?: "Invalid request")
        }
    }

    fun handleAddNewRoom(data: AddNewData): Response {
        try {
            val roomID: ByteArray = data.id
            val roomName: String = data.name

            return AppService.addNewRoom(roomName, roomID)

        } catch (e: Exception) {
            return Response(Status.ERROR, e.message ?: "Invalid request")
        }
    }


}