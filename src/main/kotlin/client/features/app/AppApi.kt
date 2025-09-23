package client.features.app

import client.network.SecureClientConnection
import kotlinx.serialization.json.Json
import shared.protocol.Command
import shared.protocol.Request
import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.AddNewData
import shared.protocol.data.ClientData
import shared.protocol.data.RoomList

object AppApi {
    fun getRoomList(): List<String> {

        val request = Request(Command.GET_CLASSES_LIST)
        val response: Response = SecureClientConnection.sendCommand(request)

        return when (response.status) {
            Status.SUCCESS -> (response.data as RoomList).roomList
            Status.FAIL -> listOf("${response.message}")
            Status.ERROR -> listOf("${response.message}")
        }
    }

    fun getAttendanceList(roomName: String): Json = TODO()

    fun checkInStudent(studentID: ByteArray, roomID: String): Response = TODO()

    fun addNewStudent(studentName: String, studentID: ByteArray): Response {
        val request = Request(Command.ADD_NEW_STUDENT, AddNewData(studentName, studentID))
        val response = SecureClientConnection.sendCommand(request)
        return response
    }

    fun addNewRoom(roomName: String, roomID: ByteArray): Response {
        val request = Request(Command.ADD_NEW_ROOM, AddNewData(roomName, roomID))
        val response = SecureClientConnection.sendCommand(request)
        return response
    }

    fun addNewStudentAndNewRoom(studentName: String, studentID: ByteArray, roomName: String, roomID: ByteArray) {
        addNewStudent(studentName, studentID)
        addNewRoom(roomName, roomID)
    }
}