package server.database.dao

import server.database.DBResult
import server.database.DatabaseManager
import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.ClassesList
import java.sql.SQLException

object ClassesDAO {
    fun getAllClasses() : DBResult {
        val sql = "SELECT * FROM classes"
        try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    val rs = stmt.executeQuery()

                    val classesList = mutableListOf<String>()

                    while (rs.next()) {
                        classesList.add(rs.getString(2))
                    }
                   return DBResult.WithData(ClassesList(classesList))
                }
            }
        } catch (e: SQLException) {
            return DBResult.ResponseOnly(Response(Status.ERROR, e.message ?: "Database error"))
        } catch (e: Exception) {
            return DBResult.ResponseOnly(Response(Status.ERROR, e.message ?: "Unexpected error"))
        }
    }
}