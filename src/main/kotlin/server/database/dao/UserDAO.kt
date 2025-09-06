package server.database.dao

import server.database.DatabaseManager
import server.database.DBResult
import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.UserData
import java.sql.SQLException
import kotlin.use

object UserDAO {
    fun insertUser(userName: String, password: String, salt: String): DBResult.ResponseOnly {
        val sql = "INSERT INTO users(userName, password, salt) VALUES (?, ?, ?)"
        try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, userName)
                    stmt.setString(2, password)
                    stmt.setString(3, salt)
                    stmt.executeUpdate()
                    return DBResult.ResponseOnly(Response(Status.SUCCESS))
                }
            }
        } catch (e: Exception) {
            println("Insert failed: ${e.message}")
            return DBResult.ResponseOnly(Response(Status.FAIL, e.message))
        }
    }

    fun findByUserName(userName: String): DBResult {
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
                        DBResult.WithData(UserData(userName, password, salt))
                    } else {
                        DBResult.ResponseOnly(Response(Status.FAIL, "Wrong username or password"))
                    }
                }
            }
        } catch (e: SQLException) {
            DBResult.ResponseOnly(Response(Status.ERROR, e.message ?: "Database error"))
        } catch (e: Exception) {
            DBResult.ResponseOnly(Response(Status.ERROR, e.message ?: "Unexpected error"))
        }
    }
}