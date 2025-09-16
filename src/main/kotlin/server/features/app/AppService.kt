package server.features.app

import server.database.DBResult
import server.database.dao.RoomDAO
import shared.protocol.Response
import shared.protocol.Status

object AppService {

    fun getRoomList(): Response {
        println("getting classesList")
        return when (val query = RoomDAO.getRoomList()) {
            is DBResult.WithData -> {
                Response(Status.SUCCESS, null, query.data)
            }

            is DBResult.ResponseOnly -> {
                query.response
            }
        }
    }

    fun getAttendanceList(roomName: String): Response = TODO()

    fun checkInStudent(roomID: String, studentID: ByteArray): Response = TODO()

    fun addNewStudent(studentName: String, studentID: ByteArray): Response = TODO()

    fun addNewRoom(roomName: String, roomID: String): Response = TODO()

}

