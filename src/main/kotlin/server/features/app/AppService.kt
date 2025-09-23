package server.features.app

import server.database.DBResult
import server.database.dao.RoomDAO
import server.database.dao.StudentDAO
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

    fun addNewStudent(studentName: String, studentID: ByteArray): Response {
        StudentDAO.insertStudent(studentName, studentID)

        return if (StudentDAO.checkIfStudentExists(studentID)) {
            Response(Status.SUCCESS)
        } else Response(Status.FAIL)
    }

    fun addNewRoom(roomName: String, roomID: ByteArray): Response {
        RoomDAO.insertRoom(roomID, roomName)

        return if (RoomDAO.checkIfRoomExists(roomID)) {
            Response(Status.SUCCESS)
        } else Response(Status.FAIL)
    }

}

