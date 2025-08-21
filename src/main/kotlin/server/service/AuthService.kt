package server.service

import server.database.DBHelper
import server.database.DBResult
import server.util.PasswordHasher
import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.UserData

object AuthService {
    init {
        DBHelper.createTable()
    }

    fun signIn(userName: String, password: String): Response {
        val query: DBResult = DBHelper.getUser(userName)
        return when (query) {
            is DBResult.ResponseOnly -> query.response
            is DBResult.WithData -> {
                val data = query.data as UserData
                val storedPass = data.storedPassword
                val salt = data.salt
                if (PasswordHasher.verifyPassword(password, salt, storedPass)) {
                    Response(Status.SUCCESS)
                } else Response(Status.FAIL, "Wrong username or password")
            }
        }
    }

    fun signUp(userName: String, password: String): Response {
        val query: DBResult = DBHelper.getUser(userName)

        return when (query) {
            is DBResult.WithData -> Response(Status.FAIL, "username already taken")
            is DBResult.ResponseOnly -> {
                val status = query.response.status
                if (status != Status.FAIL) {
                    query.response
                } else {
                    val salt = PasswordHasher.generateSalt()
                    val hashedPassword = PasswordHasher.hashPassword(password, salt)
                    val insertResult = DBHelper.insertUser(userName, hashedPassword, salt)
                    insertResult.response
                }
            }
        }
    }

}