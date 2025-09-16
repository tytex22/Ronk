package server.features.app

import shared.protocol.Response
import shared.protocol.Status

object AppApi {
    fun handleGetRoomList(): Response {
        return try {
            println("classesApi")
            AppService.getRoomList()
        } catch (e: Exception) {
            Response(Status.ERROR, e.message ?: "Invalid request")
        }
    }

    fun handleGetAttendanceList(roomName: String): Response = TODO()

    fun handleCheckInStudent(roomID: String, studentID: ByteArray): Response = TODO()

    fun handleAddNewStudent(studentName: String, studentID: ByteArray): Response = TODO()

    fun handleAddNewRoom(roomName: String, roomID: String): Response = TODO()


}