package server.database.dao

import server.database.DBResult
import server.database.DatabaseManager
import java.sql.SQLException
import kotlin.use

object StudentDAO {
    fun insertStudent(studentName: String, studentID: ByteArray) {
        val sql = "INSERT INTO students(studentID, studentName) VALUES (?, ?)"
        try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setBytes(1, studentID)
                    stmt.setString(2, studentName)
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

    fun checkIfStudentExists(studentID: ByteArray): Boolean {
        val sql = "SELECT 1 FROM students WHERE studentName = ? LIMIT 1"
        return try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setBytes(1, studentID)
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



}