package server.database

import server.database.DatabaseManager.insertClass
import shared.protocol.Response
import shared.protocol.Status
import java.sql.Connection
import java.sql.DriverManager

object  DatabaseManager {
    private const val DB_URL = "jdbc:sqlite:src/main/kotlin/server/database/database.db"

    init {
        // Load the SQLite JDBC driver (not always necessary with recent versions)
        Class.forName("org.sqlite.JDBC")
    }

    fun getConnection(): Connection {
        return DriverManager.getConnection(DB_URL)
    }

    fun createTable() {
        val sql = """
            CREATE TABLE IF NOT EXISTS users (
                userName TEXT UNIQUE PRIMARY KEY,
                password TEXT NOT NULL,
                salt TEXT NOT NULL
            );
            
            CREATE TABLE IF NOT EXISTS users (
                classID BLOB UNIQUE PRIMARY KEY,
                className TEXT NOT NULL
            );
        """.trimIndent()

        getConnection().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute(sql)
            }
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