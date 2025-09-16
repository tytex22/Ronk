package server.database.dao

import server.database.DBResult
import server.database.DatabaseManager
import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.RoomList
import java.sql.SQLException

object RoomDAO {
    fun getRoomList() : DBResult {
        val sql = "SELECT * FROM classes"
        try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    val rs = stmt.executeQuery()

                    val classesList = mutableListOf<String>()

                    while (rs.next()) {
                        classesList.add(rs.getString(2))
                    }
                   return DBResult.WithData(RoomList(classesList))
                }
            }
        } catch (e: SQLException) {
            return DBResult.ResponseOnly(Response(Status.ERROR, e.message ?: "Database error"))
        } catch (e: Exception) {
            return DBResult.ResponseOnly(Response(Status.ERROR, e.message ?: "Unexpected error"))
        }
    }
    // List<String>?

    fun getAttendanceList(roomName: String) : DBResult {TODO()} // JSON?
    fun getStudentIfExist(studentID: ByteArray) : DBResult {TODO()} // Student?
    fun getRoomIfExist(roomID: String) : DBResult {TODO()} // Room?
    fun addNewStudent(studentName: String, studentID: ByteArray) : DBResult {TODO()}
    fun addNewRoom(roomName: String, roomID: String) : DBResult {TODO()}

}