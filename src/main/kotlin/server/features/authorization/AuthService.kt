package server.features.authorization

import server.database.DatabaseManager
import server.database.DBResult
import server.database.dao.UserDAO
import server.util.PasswordHasher
import server.util.Validation
import shared.protocol.Response
import shared.protocol.Status
import shared.protocol.data.UserData

object AuthService {
    init {
        DatabaseManager.createTable()
    }

    fun signIn(userName: String, password: String): Response {
        when (val query: DBResult = UserDAO.findByUserName(userName)) {
            is DBResult.ResponseOnly -> return query.response
            is DBResult.WithData -> {
                val data = query.data as UserData
                val storedPass = data.storedPassword
                val salt = data.salt
                return if (PasswordHasher.verifyPassword(password, salt, storedPass)) {
                    Response(Status.SUCCESS)
                } else Response(Status.FAIL, "Wrong username or password")
            }
        }
    }

    fun signUp(userName: String, password: String): Response {

        val (isStrong, notStrongReason) = Validation.isStrongEnough(password)

        if (!isStrong) return Response(Status.FAIL, notStrongReason)

        when (val query: DBResult = UserDAO.findByUserName(userName)) {
            is DBResult.WithData -> return Response(Status.FAIL, "username already taken")
            is DBResult.ResponseOnly -> {
                val status = query.response.status
                if (status != Status.FAIL) {
                    return query.response
                } else {
                    val salt = PasswordHasher.generateSalt()
                    val hashedPassword = PasswordHasher.hashPassword(password, salt)
                    val insertResult = UserDAO.insertUser(userName, hashedPassword, salt)
                    return insertResult.response
                }
            }
        }

    }

}