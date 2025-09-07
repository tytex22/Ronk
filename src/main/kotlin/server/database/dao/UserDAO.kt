package server.database.dao

import server.database.DatabaseManager
import server.database.entities.UserDTO
import java.sql.SQLException

object UserDAO {
    fun insertUser(userName: String, password: String, salt: String) {
        val sql = "INSERT INTO users(userName, password, salt) VALUES (?, ?, ?)"
        try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, userName)
                    stmt.setString(2, password)
                    stmt.setString(3, salt)
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

    fun checkIfUserExistByName(userName: String): Boolean {
        val sql = "SELECT 1 FROM users WHERE username = ? LIMIT 1"
        return try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, userName)
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

    fun getByUserName(userName: String): UserDTO {
        val sql = "SELECT userName, password, salt FROM users WHERE username = ?"
        return try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, userName)
                    val rs = stmt.executeQuery()
                    if (rs.next()) {
                        val userName = rs.getString("userName")
                        val password = rs.getString("password")
                        val salt = rs.getString("salt")

                        UserDTO(userName, password, salt)
                    } else {
                        throw IllegalStateException("User '$userName' was expected to exist but was not found")
                    }
                }
            }
        } catch (e: SQLException) {
            throw RuntimeException("Database error: ${e.message}", e)

        } catch (e: Exception) {
            throw RuntimeException("Unexpected error: ${e.message}", e)
        }
    }

}