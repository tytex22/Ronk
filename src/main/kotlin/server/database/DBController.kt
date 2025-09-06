package server.database

import server.database.DBController.insertClass
import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.UserData
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object  DBController {
    private const val DB_URL = "jdbc:sqlite:src/main/kotlin/server/database/database.db"

    init {
        // Load the SQLite JDBC driver (not always necessary with recent versions)
        Class.forName("org.sqlite.JDBC")
    }

    private fun getConnection(): Connection {
        return DriverManager.getConnection(DB_URL)
    }

    fun createTable() {
        val sql = """
            CREATE TABLE IF NOT EXISTS users (
                userName TEXT UNIQUE PRIMARY KEY,
                password TEXT NOT NULL,
                salt TEXT NOT NULL
            );
        """.trimIndent()

        getConnection().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute(sql)
            }
        }
    }

    fun insertUser(userName: String, password: String, salt: String): DBResult.ResponseOnly {
        val sql = "INSERT INTO users(userName, password, salt) VALUES (?, ?, ?)"
        return try {
            getConnection().use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, userName)
                    stmt.setString(2, password)
                    stmt.setString(3, salt)
                    stmt.executeUpdate()
                    DBResult.ResponseOnly(Response(Status.SUCCESS))
                }
            }
        } catch (e: Exception) {
            println("Insert failed: ${e.message}")
            DBResult.ResponseOnly(Response(Status.FAIL, e.message))
        }
    }

    fun getUser(userName: String): DBResult {
        val sql = "SELECT userName, password, salt FROM users WHERE username = ?"
        return try {
            getConnection().use { conn ->
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

    fun insertClass(classID: Short, className: String): DBResult {
        val cl = classID.toBlob()
        val sql = "INSERT INTO classes(classID, className) VALUES (?, ?)"
        return try {
            getConnection().use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setBytes(1, cl)
                    stmt.setString(2, className)
                    stmt.executeUpdate()
                    DBResult.ResponseOnly(Response(Status.SUCCESS))
                }
            }
        } catch (e: Exception) {
            println("Insert failed: ${e.message}")
            DBResult.ResponseOnly(Response(Status.FAIL, e.message))
        }
    }

    fun Short.toBlob(): ByteArray {
        return byteArrayOf(
            ((toInt() shr 8) and 0xFF).toByte(),
            (toInt() and 0xFF).toByte()
        )
    }


}

fun main() {
    insertClass(12345, "Test")
}