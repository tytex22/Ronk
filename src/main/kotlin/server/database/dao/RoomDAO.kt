package server.database.dao

import server.database.DBResult
import server.database.DatabaseManager
import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.RoomList
import java.sql.SQLException

object RoomDAO {
    fun getRoomList() : DBResult {
        val sql = "SELECT * FROM rooms"
        try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    val rs = stmt.executeQuery()

                    val roomList = mutableListOf<String>()

                    while (rs.next()) {
                        roomList.add(rs.getString(2))
                    }
                   return DBResult.WithData(RoomList(roomList))
                }
            }
        } catch (e: SQLException) {
            return DBResult.ResponseOnly(Response(Status.ERROR, e.message ?: "Database error"))
        } catch (e: Exception) {
            return DBResult.ResponseOnly(Response(Status.ERROR, e.message ?: "Unexpected error"))
        }
    }
    // List<String>?

    fun checkIfRoomExists(roomID: ByteArray) : Boolean {
        val sql = "SELECT 1 FROM rooms WHERE roomID = ? LIMIT 1"
        return try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setBytes(1, roomID)
                    val rs = stmt.executeQuery()
                    rs.next()
                }
            }
        } catch (e: SQLException) {
            throw RuntimeException("Database error: ${e.message}", e)
        } catch (e: Exception) {
            throw RuntimeException("Unexpected error: ${e.message}", e)
        }
    }

    fun insertRoom(roomID: ByteArray, roomName: String) {
        val sql = "INSERT INTO rooms(roomID, roomName) VALUES (?, ?)"
        try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setBytes(1, roomID)
                    stmt.setString(2, roomName)
                    val rowsAffected = stmt.executeUpdate()
                    if (rowsAffected != 1) {
                        throw SQLException("No rows were inserted")
                    }
                }
            }
        } catch (e: Exception) {
            println("Insert failed: ${e.message}")
            throw RuntimeException("Failed to insert user: ${e.message}", e)
        }
    }
    fun getRoomIfExist(roomID: String) : DBResult {TODO()} // Room?
    fun addNewRoom(roomName: String, roomID: String) : DBResult {TODO()}

}